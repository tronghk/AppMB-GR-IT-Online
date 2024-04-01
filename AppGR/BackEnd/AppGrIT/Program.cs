using AppGrIT.Authentication;
using AppGrIT.Data;
using AppGrIT.Services;
using AppGrIT.Services.AppGrIT.Services;
using AppGrIT.Services.Imployement;
using FirebaseAdmin;
using Microsoft.AspNetCore.Authentication;
using Microsoft.AspNetCore.Authentication.JwtBearer;
using Microsoft.Extensions.FileProviders;
using Microsoft.IdentityModel.Tokens;
using System.Text;

var builder = WebApplication.CreateBuilder(args);

// Add services to the container.

builder.Services.AddControllers();
// Learn more about configuring Swagger/OpenAPI at https://aka.ms/aspnetcore/swashbuckle
builder.Services.AddEndpointsApiExplorer();
builder.Services.AddSwaggerGen();

builder.Services.AddAuthentication(options =>
{
    options.DefaultAuthenticateScheme = JwtBearerDefaults.AuthenticationScheme;
    options.DefaultChallengeScheme = JwtBearerDefaults.AuthenticationScheme;
    options.DefaultScheme = JwtBearerDefaults.AuthenticationScheme;
})
    // khai báo thời gian sống , lưu trữ token
    .AddJwtBearer(options =>
    {
        options.SaveToken = true;
        options.RequireHttpsMetadata = false;


        // lấy token
        options.TokenValidationParameters = new Microsoft.IdentityModel.Tokens.TokenValidationParameters
        {
            ValidateIssuer = true,
            ValidateAudience = true,
            ValidateLifetime = true,
            ValidateIssuerSigningKey = true,
            ClockSkew = TimeSpan.Zero,
            ValidIssuer = builder.Configuration["JWT:ValidIssuer"],
            ValidAudience = builder.Configuration["JWT:ValidAudience"],


            //key
            IssuerSigningKey = new SymmetricSecurityKey(Encoding.UTF8
            .GetBytes(builder.Configuration["JWT:Secret"]!))
        };
    });

builder.Services.AddControllers().AddNewtonsoftJson();
builder.Services.AddScoped<ConnectFirebase>();
builder.Services.AddScoped<UsersDAO>();
builder.Services.AddScoped<RolesDAO>();
builder.Services.AddScoped<PostsDAO>();
builder.Services.AddScoped<ImagesDAO>();
builder.Services.AddScoped<PostCommentsDAO>();
builder.Services.AddScoped<PostExpressionssDAO>();
builder.Services.AddScoped<UserFollowsDAO>();
builder.Services.AddScoped<UserFriendsDAO>();
builder.Services.AddScoped<UnUserDAO>();
builder.Services.AddScoped<IToken, TokenServices>();
builder.Services.AddScoped<IUsers, UserServices>();
builder.Services.AddScoped<IRoles, RoleServices>();
builder.Services.AddScoped<IPosts, PostServices>();
builder.Services.AddScoped<IImages, ImageServices>();
builder.Services.AddScoped<IPostComments, PostCommentServices>();
builder.Services.AddScoped<IPostExpressionss, PostExpressionServicess>();
builder.Services.AddScoped<IUserFollows, UserFollowsServices>();
builder.Services.AddScoped<IUserFriends, UserFriendsServices>();
builder.Services.AddScoped<IUnUser, UnUserServices>();
builder.Services.AddCors(options =>
{
    options.AddDefaultPolicy(
            policy =>
            {
                policy.AllowAnyOrigin()
                .AllowAnyMethod()
                .AllowAnyHeader();

            }
        );
});
//builder.WebHost.UseUrls("www.AppGRIT.somee.com");
var app = builder.Build();

// Configure the HTTP request pipeline.
if (app.Environment.IsDevelopment())
{
    app.UseSwagger();
    app.UseSwaggerUI();
}

app.UseHttpsRedirection();

app.UseAuthorization();

app.MapControllers();
//thêm dòng này để truyền json thành công
app.UseCors();
// sử dụng wwwroot
app.Run();

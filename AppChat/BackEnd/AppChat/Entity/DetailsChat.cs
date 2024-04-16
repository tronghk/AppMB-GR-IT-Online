﻿namespace AppChat.Entity
{
    public class DetailsChat
    {
        public string ChatId { get; set; }
        public string UserId
        { get; set; }

        public string DetailId
        { get; set; }
        public string Status
        { get; set; }
        public DateTime Time
        { get; set; }
        public string Content
        { get; set; }
        public string ImagePath
        { get; set; }
    }
}

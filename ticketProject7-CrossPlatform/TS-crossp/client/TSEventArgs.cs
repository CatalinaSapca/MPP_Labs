using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace client
{
    public enum TSUserEvent
    {
        BoughtTicket
    };
    public class UserEventArgs : EventArgs
    {
        private readonly TSUserEvent userEvent;
        private readonly Object data;

        public UserEventArgs(TSUserEvent userEvent, object data)
        {
            this.userEvent = userEvent;
            this.data = data;
        }

        public TSUserEvent UserEventType
        {
            get { return userEvent; }
        }

        public object Data
        {
            get { return data; }
        }
    }
}

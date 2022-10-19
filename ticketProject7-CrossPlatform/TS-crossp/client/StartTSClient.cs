using System;
using System.Collections;
using System.Collections.Generic;
using System.Windows.Forms;
using services;
using networking;
using Hashtable = System.Collections.Hashtable;


namespace client
{
    static class StartChatClient
    {
        [STAThread]
        static void Main()
        {
            Application.EnableVisualStyles();
            Application.SetCompatibleTextRenderingDefault(false);


            //IChatServer server=new ChatServerMock();          
            //ITicketSystemServices server = new TicketServerProxy("127.0.0.1", 55555);
            //WindowCtrl ctrl = new WindowCtrl(server);
            ////LoginWindow win = new LoginWindow(ctrl);
            //Application.Run(win);



            ITicketSystemServices server = new TicketServerProxy("127.0.0.1", 55555);
            WindowCtrl ctrl = new WindowCtrl(server);
            Window win = new Window(ctrl);

            //Application.EnableVisualStyles();
            //Application.SetCompatibleTextRenderingDefault(false);

            Application.Run(win);
        }
    }
}

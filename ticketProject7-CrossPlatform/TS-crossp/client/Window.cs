using model;
using model.Validators;
using networking.dto;
using services;
using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace client
{
    public partial class Window : Form
    {
        DataSet ds = new DataSet();
        private readonly WindowCtrl ctrl;
        private IEnumerable<ArtisticRepresentation> dataSource;
        public Window(WindowCtrl ctrl)
        {
            InitializeComponent();

            //this.paneLogin.BringToFront();
            this.ctrl = ctrl;
            ctrl.updateEvent += userUpdate;
        }

        private void Form1_Load(object sender, EventArgs e)
        {

        }

        private void panel1_Paint(object sender, PaintEventArgs e)
        {

        }

        //---
        public void populateAllArtistTable()
        {
            dataGridViewAll.DataSource = null;
            dataGridViewAll.Rows.Clear();
            dataSource = ctrl.getAllAR();
            dataGridViewAll.DataSource = dataSource;

            //make rows red if they don't have availableSeats
            foreach (DataGridViewRow row in dataGridViewAll.Rows)
                if (Convert.ToInt32(row.Cells[3].Value) == 0)
                    row.DefaultCellStyle.BackColor = Color.IndianRed;
                else
                    row.DefaultCellStyle.BackColor = Color.White;


            //DataGridViewRow selectedRow = dataGridViewAll.Rows[0];
            //MessageBox.Show("0" + selectedRow.Cells[0].Value.ToString());
            //MessageBox.Show("1" + selectedRow.Cells[1].Value.ToString());
            //MessageBox.Show("2" + selectedRow.Cells[2].Value.ToString());
            //MessageBox.Show("3" + selectedRow.Cells[3].Value.ToString());
            //MessageBox.Show("4" + selectedRow.Cells[4].Value.ToString());
            //MessageBox.Show("5" + selectedRow.Cells[5].Value.ToString());
            //MessageBox.Show("6" + selectedRow.Cells[6].Value.ToString());
        }



        public void userUpdate(object sender, UserEventArgs e)
        {
            Console.WriteLine("sunt in userUpdayeeeeeee");
            if (e.UserEventType == TSUserEvent.BoughtTicket)
            {
                ArtisticRepresentation ar = (ArtisticRepresentation)e.Data;

                //update table all
                List<ArtisticRepresentation> ars = (List<ArtisticRepresentation>)dataGridViewAll.DataSource;

                if (ars != null)
                {
                    ArtisticRepresentation d = ar;
                    foreach (ArtisticRepresentation a in ars)
                        if (a.Id.Equals(ar.Id))
                            d = a;
                    ars.Remove(d);
                    ars.Add(ar);
                }
                
                Console.WriteLine("[Window] Bought ticket " + ar);
                dataGridViewAll.Invoke(new UpdateDataGridView(this.updateDataGridView), new Object[] { dataGridViewAll, ars });
                //   friendList.BeginInvoke((Action) delegate { friendList.DataSource = friendsData; });

                //update search table
                List<ArtisticRepresentation> arrs = (List<ArtisticRepresentation>)dataGridViewSearch.DataSource;
                if (arrs != null)
                {
                    ArtisticRepresentation dd = ar;
                    foreach (ArtisticRepresentation a in arrs)
                        if (a.Id.Equals(ar.Id))
                            dd = a;
                    arrs.Remove(dd);
                    arrs.Add(ar);
                }

                dataGridViewSearch.Invoke(new UpdateDataGridView(this.updateDataGridView), new Object[] { dataGridViewSearch, arrs });
                //   friendList.BeginInvoke((Action) delegate { friendList.DataSource = friendsData; });

            }
        }
        //for updating the GUI

        //1. define a method for updating the DataGridView
        private void updateDataGridView(DataGridView dataGridView, IEnumerable<ArtisticRepresentation> newData)
        {
            dataGridView.DataSource = null;
            dataGridView.DataSource = newData;

            //make rows red if they don't have availableSeats
            foreach (DataGridViewRow row in dataGridView.Rows)
                if (Convert.ToInt32(row.Cells[3].Value) == 0)
                    row.DefaultCellStyle.BackColor = Color.IndianRed;
                else
                    row.DefaultCellStyle.BackColor = Color.White;
        }

        //2. define a delegate to be called back by the GUI Thread
        public delegate void UpdateDataGridView(DataGridView dataGridView, IEnumerable<ArtisticRepresentation> data);



        //3. in the other thread call like this:
        /*
         * list.Invoke(new UpdateListBoxCallback(this.updateListBox), new Object[]{list, data});
         * 
         * */


        private void logOutToolStripMenuItem_Click(object sender, EventArgs e)
        {
            this.paneLogin.Visible = true;
            clearFields();
            ctrl.logout();
        }

        private void clearFields()
        {
            this.textBoxUsername.Text = "";
            this.textBoxPassword.Text = "";
            this.textBoxFName.Text = "";
            this.textBoxLName.Text = "";
            this.numericUpDown1.Value = 0;
            dataGridViewAll.DataSource = null;
            dataGridViewAll.Rows.Clear();
            dataGridViewSearch.DataSource = null;
            dataGridViewSearch.Rows.Clear();
        }

        //handle Log in button pressed
        private void buttonLogin_Click(object sender, EventArgs e)
        {
            try
            {
                String username = this.textBoxUsername.Text;
                String password = this.textBoxPassword.Text;

                Seller seller = ctrl.login(username, password);
                if (seller != null)
                {
                    if (seller.Password == password)
                    {

                        this.paneLogin.Visible = false;
                        populateAllArtistTable();
                    }
                    else
                        MessageBox.Show("Invalid password");
                }
                else
                    MessageBox.Show("Invalid username");

            }
            catch (ValidationException ex)
            {
                MessageBox.Show(ex.Message);
            }
            catch (ServicesException ex)
            {
                MessageBox.Show(ex.Message);
            }
        }

        private void Window_FormClosed(object sender, FormClosedEventArgs e)
        {
            Console.WriteLine("TicketSystem closing " + e.CloseReason);
            if (e.CloseReason == CloseReason.UserClosing)
            {
                ctrl.logout();
                ctrl.updateEvent -= userUpdate;
                Application.Exit();
            }
        }

        //handle Search by date button pressed---
        private void buttonSearch_Click(object sender, EventArgs e)
        {
            if (dateTimePicker1.Value == null)
                MessageBox.Show("Please select a date\n");
            DateTime filterDate = dateTimePicker1.Value;
            IEnumerable<ArtisticRepresentation> allAR = ctrl.getAllARFromDate(dateTimePicker1.Value);
            dataGridViewSearch.DataSource = allAR;

            //make rows red if they don't have availableSeats
            foreach (DataGridViewRow row in dataGridViewSearch.Rows)
                if (Convert.ToInt32(row.Cells[3].Value) == 0)
                    row.DefaultCellStyle.BackColor = Color.IndianRed;
                else
                    row.DefaultCellStyle.BackColor = Color.White;
        }


        //handle Buy
        private void buttonBuy_Click(object sender, EventArgs e)
        {
            if (this.textBoxFName.Text == null || this.textBoxLName.Text == null || this.numericUpDown1.Value == 0)
                MessageBox.Show("Complete all fields!");
            else
            {
                String firstName = this.textBoxFName.Text;
                String lastName = this.textBoxLName.Text;
                int noTickets = Convert.ToInt32(this.numericUpDown1.Value);
                if (noTickets <= 0)
                    MessageBox.Show("Invalid number of tickets!");
                else
                {
                    if (dataGridViewSearch.SelectedRows.Count != 1)
                        MessageBox.Show("Select one and only one concert!!");
                    else
                    {
                        DataGridViewRow selectedRow = dataGridViewSearch.SelectedRows[0];
                        int id = Convert.ToInt32(selectedRow.Cells[5].Value);
                        string artistName = selectedRow.Cells[0].Value.ToString();
                        DateTime data = (DateTime)selectedRow.Cells[1].Value;
                        string location = selectedRow.Cells[2].Value.ToString();
                        int availableSeats = Convert.ToInt32(selectedRow.Cells[3].Value);
                        int soldSeats = Convert.ToInt32(selectedRow.Cells[4].Value);
                        ArtisticRepresentation ar = new ArtisticRepresentation(artistName, data, location, availableSeats, soldSeats);
                        ar.Id = id;

                        if (availableSeats == 0)
                            MessageBox.Show("Unavailable", "No available tickets for this concert!");
                        else if (noTickets > availableSeats)
                            MessageBox.Show("Unavailable", "Select a smaller number of tickets!!");
                        else
                        {
                            ar.AvailableSeats = availableSeats - noTickets;
                            ar.SoldSeats = ar.SoldSeats + noTickets;
                            if (this.ctrl.updateAR(ar) == null)
                            {
                                MessageBox.Show("Succes", "Successfully bought!");
                                this.textBoxFName.Text = "";
                                this.textBoxLName.Text = "";
                                this.numericUpDown1.Value = 0;

                                //Buyer buyer = new Buyer(ar.Id, firstName, lastName, noTickets);
                                //buyer.Id = 1;
                                //ctrl.addBuyer(buyer);

                                //clear searchTable
                                dataGridViewSearch.DataSource = null;
                                dataGridViewSearch.Rows.Clear();

                                //update the all Concerts table
                                //populateAllArtistTable();

                            }
                            else
                                MessageBox.Show("Something went wrong!");
                        }
                    }
                }

            }
        }

        private void Menu_Click(object sender, EventArgs e)
        {

        }


    }
}
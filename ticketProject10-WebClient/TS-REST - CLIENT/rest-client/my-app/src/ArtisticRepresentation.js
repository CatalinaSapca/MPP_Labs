import React from  'react';
import './ArtisticRepresentationApp.css'

class ARRow extends React.Component{

    handleClickDelete=(event)=>{
        console.log('delete button pentru '+this.props.ar.id);
        this.props.deleteFunc(this.props.ar.id);
    }

    render() {
        return (
            <tr>
                <td>{this.props.ar.id}</td>
                <td >{this.props.ar.artistName}</td>
                <td >{this.props.ar.data}</td>
                <td >{this.props.ar.location}</td>
                <td >{this.props.ar.availableSeats}</td>
                <td >{this.props.ar.soldSeats}</td>
                <td><button  onClick={this.handleClickDelete}>Delete</button></td>
            </tr>
        );
    }
}
// <form onSubmit={this.handleClicke}><input type="submit" value="Delete"/></form>

class ARTable extends React.Component {
    render() {
        var rows = [];
        var functieStergere=this.props.deleteFunc;
        console.log(this.props.ars);
        this.props.ars.forEach(function(ar) {
            rows.push(<ARRow ar={ar} key={ar.id} deleteFunc={functieStergere} />);
        });
        return (<div className="ARTable">

            <table className="center">
                <thead>
                <tr>
                    <th>ID</th>
                    <th>artist name</th>
                    <th>data</th>
                    <th>location</th>
                    <th>available seats</th>
                    <th>sold seats</th>
                </tr>
                </thead>
                <tbody>{rows}</tbody>
            </table>

            </div>
        );
    }
}

export default ARTable;
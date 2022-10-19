import React from  'react';
class ARForm extends React.Component{

    constructor(props) {
        super(props);
        this.state = {id: '', artistName:'', data:'', location:'', availableSeats:'', soldSeats:''};

       // this.handleChange = this.handleChange.bind(this);
       // this.handleSubmit = this.handleSubmit.bind(this);
    }

    handleIdChange=(event) =>{
        this.setState({id: event.target.value});
    }

    handleArtistNameChange=(event) =>{
        this.setState({artistName: event.target.value});
    }

    handleDataChange=(event) =>{
        this.setState({data: event.target.value});
    }

    handleLocationChange=(event) =>{
        this.setState({location: event.target.value});
    }

    handleAvailableSeatsChange=(event) =>{
        this.setState({availableSeats: event.target.value});
    }

    handleSoldSeatsChange=(event) =>{
        this.setState({soldSeats: event.target.value});
    }
    handleSubmit =(event) =>{

        var ar={id:this.state.id,
                artistName:this.state.artistName,
                data:this.state.data,
                location: this.state.location,
                availableSeats: this.state.availableSeats,
                soldSeats: this.state.soldSeats
        }
        console.log('A ArtisticRepresentation was submitted: ');
        console.log(ar);
        this.props.addFunc(ar);
        event.preventDefault();
    }

    handleSubmitUpdate =(event) =>{

        var ar={id:this.state.id,
            artistName:this.state.artistName,
            data:this.state.data,
            location: this.state.location,
            availableSeats: this.state.availableSeats,
            soldSeats: this.state.soldSeats
        }
        console.log('A ArtisticRepresentation was updated: ');
        console.log(ar);
        this.props.updateFunc(ar);
        event.preventDefault();
    }

    render() {
        return (
            <form onSubmit={this.handleSubmit}>
                <label>
                    Id:
                    <input type="text" value={this.state.id} onChange={this.handleIdChange}/>
                </label><br/>
                <label>
                    Artist Name:
                    <input type="text" value={this.state.artistName} onChange={this.handleArtistNameChange} />
                </label><br/>
                <label>
                    Data:
                    <input type="text" value={this.state.data} onChange={this.handleDataChange} />
                </label><br/>
                <label>
                    Location:
                    <input type="text" value={this.state.location} onChange={this.handleLocationChange} />
                </label><br/>
                <label>
                    Available seats:
                    <input type="number" value={this.state.availableSeats} onChange={this.handleAvailableSeatsChange} />
                </label><br/>
                <label>
                    Sold seats::
                    <input type="number" value={this.state.soldSeats} onChange={this.handleSoldSeatsChange} />
                </label><br/>

                <input type="submit" value="ADD"/>
                <input type="button" value="UPDATE" onClick={this.handleSubmitUpdate}/>
            </form>
        );
    }



}
export default ARForm;
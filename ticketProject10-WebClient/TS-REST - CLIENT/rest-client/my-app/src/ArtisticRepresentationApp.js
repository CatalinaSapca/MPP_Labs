import React from  'react';
import ARTable from './ArtisticRepresentation';
import './ArtisticRepresentationApp.css'
import ARForm from "./ARForm";
import {UpdateAR, DeleteAR, AddAR, GetARs} from './utils/rest-calls'


class ArtisticRepresentationApp extends React.Component{
    constructor(props){
        super(props);
        // this.state={users:[{"passwd":"maria","name":"Marinescu Maria","id":"maria"}],
        //     deleteFunc:this.deleteFunc.bind(this),
        //     addFunc:this.addFunc.bind(this),
        // }
        this.state={ars:[],
            deleteFunc:this.deleteFunc.bind(this),
            addFunc:this.addFunc.bind(this),
            updateFunc:this.updateFunc.bind(this)
        }
        //this.state={}
        console.log('ArtisticRepresentationApp constructor')
    }

    addFunc(ar){
        console.log('inside add Func '+ar);
        AddAR(ar)
            .then(res=> GetARs())
            .then(ars=>this.setState({ars}))
            .catch(error=>console.log('error add ',error));
    }


    deleteFunc(ar){
        console.log('inside deleteFunc '+ar);
        DeleteAR(ar)
            .then(res=> GetARs())
            .then(ars=>this.setState({ars}))
            .catch(error=>console.log('error delete', error));
    }

    updateFunc(ar){
        console.log('inside updateFunc '+ar);
        UpdateAR(ar)
            .then(res=> GetARs())
            .then(ars=>this.setState({ars}))
            .catch(error=>console.log('error update', error));
    }


    componentDidMount(){
        console.log('inside componentDidMount')
        GetARs().then(ars=>this.setState({ars}));
    }

    render(){
        return(
            <div className="ArtisticRepresentationApp">
                <h1>Ticket System Management</h1>
                <ARForm addFunc={this.state.addFunc} updateFunc={this.state.updateFunc}/>
                <br/>
                <br/>
                 <ARTable ars={this.state.ars} deleteFunc={this.state.deleteFunc} />
            </div>
        );
    }
}

export default ArtisticRepresentationApp;
// @flow

import React, { Component } from "react";
import { Page, Grid, GalleryCard, Form, Button} from "tabler-react";
import Icon from "./components/Icon";
import "tabler-react/dist/Tabler.css";
import SiteWrapper from "./SiteWrapper.react";
import getDataForm from "./Rest/getDataForm";
import Property from "./Rest/Property";

import DatePicker from "react-datepicker";
 
import "react-datepicker/dist/react-datepicker.css";


export default class GalleryPage extends Component {

  constructor(props) {
      super(props);
      this.state = {
        startDate : new Date(),
        endDate : new Date(),
        houses : [
          {id: -2,  rating:5 ,ownerName:"jbtavares" ,city:"Aveiro", description:"Casa muito humilde com tudo o que precisa!", kmFromCityCenter:3, pricePerNight:300.0, numberOfBeds: 1, maxNumberOfUsers:1, comodities: []},
          {id: -3,  rating:5 ,ownerName:"ricardoTeves" , city:"Porto", description:"Casa muito humilde com tudo o que precisa!", kmFromCityCenter:3, pricePerNight:200.0, numberOfBeds: 2, maxNumberOfUsers:1, comodities: []},
          {id: -4,  rating:5 ,ownerName:"arturmns" , city:"Lisboa", description:"Casa muito humilde com tudo o que precisa!", kmFromCityCenter:3, pricePerNight:500.0, numberOfBeds: 3, maxNumberOfUsers:1, comodities: []}
        ]
      }
      this.property_info = this.property_info.bind(this);
      this.query_searchProperty = this.query_searchProperty.bind(this);
      this.property_obj = new Property();
  }

  property_info(id) {
    localStorage.setItem('house_id', id);
    window.location.href = '/property'
    return true;
  }

  async query_searchProperty(event){
    event.preventDefault();
    let data = getDataForm(event.target);
    
    let response = await this.property_obj.get_searchProperty(data.city, data.inicio, data.fim, data.guests)
    let status = response[0]
    let houses = response[1]
    let state_houses = []
    houses.map((home) => (
      state_houses.push(home)
      )
    )

    this.setState({
      houses : state_houses
    })

  }

  handleChange(date, name){
    this.setState({
      [name]: date
    })

  };


  render(){
    const options = (
      <React.Fragment>
        <Form onSubmit={this.query_searchProperty}>
          <div class="row">
            <div class="col-lg-4">
            
              <input name="city" placeholder="Insira localização" style={{marginBottom:"30px", marginTop:"30px"}} required/>
              <input type="number" name="guests" min="0" placeholder="Número de pessoas" required/>
           
            </div>
          
            <Form.Group>
              <Form.Label>Data de Início:</Form.Label>
  
              <DatePicker
                  name="inicio"
                   selected={this.state.startDate}
                   dateFormat="dd-MM-yyyy"
                   onChange={(date) => this.handleChange(date, "startDate")}
                 />
                 <Form.Label>Data de Fim:</Form.Label>
                 <DatePicker
                    name="fim"
                    selected={this.state.endDate}
                    dateFormat="dd-MM-yyyy"
                    onChange={(date) => this.handleChange(date, "endDate")}
                  />
            </Form.Group>
            <div class="col-lg-2" style={{marginTop:"92px"}}>
            <Button 
                  type="submit"
                  color="secondary"
                  icon="search"
                />
            </div>
          </div>
          </Form>
      </React.Fragment>
    );
    return (
      <SiteWrapper>
        <Page.Content>
          <Page.Header
            title="Encontre um espaço para ficar"
            options={options}
          />
  
          <Grid.Row className="row-cards">
            {this.state.houses.map((house, key) => (
              <div class="col-lg-4" key={house.id} onClick={() => this.property_info(house.houseId)} >
                <GalleryCard >
                  <GalleryCard.Image
                    src={"demo/photos/apart_example.jpg"}
                    onClick={() => this.property_info()} 
                  />
                  <GalleryCard.Footer>
                    <GalleryCard.Details
                      avatarURL={"demo/faces/female/1.jpg"}
                      fullName={house.city}
                      dateString={house.ownerName}
                    />
                    <GalleryCard.IconGroup>
                      <Icon prefix="fa" name="male"  />  <span style={{padding : '5px'}}>{house.maxNumberOfUsers}</span>
                      <Icon prefix="fa" name="bed"  />  <span style={{padding : '5px'}}>{house.numberOfBeds}</span>
                      <Icon prefix="fa" name="star"  />  <span style={{padding : '5px'}}>{house.rating}</span>
                    </GalleryCard.IconGroup>
                  </GalleryCard.Footer>
                  <span><span style={{paddingLeft:"220px", fontSize:"25px" }}>{house.pricePerNight}€</span> /por noite</span>
                </GalleryCard>
              </div>
            ))}
          </Grid.Row>
        </Page.Content>
      </SiteWrapper>
    );
  }
  
}



// @flow

import React, { Component } from "react";
import { Page, Grid, GalleryCard, Form, Button} from "tabler-react";
import Icon from "./components/Icon";
import "tabler-react/dist/Tabler.css";
import SiteWrapper from "./SiteWrapper.react";
import getDataForm from "./Rest/getDataForm";
import Property from "./Rest/Property";

import json from "./data/Gallery.Items";
import DatePicker from "react-datepicker";
 
import "react-datepicker/dist/react-datepicker.css";


export default class GalleryPage extends Component {

  constructor(props) {
      super(props);
      this.state = {
        startDate : new Date(),
        endDate : new Date()
      }
      this.property_info = this.property_info.bind(this);
      this.query_searchProperty = this.query_searchProperty.bind(this);
      this.property_obj = new Property();
  }

  property_info() {
    //localStorage.setItem('property_id', id);
    console.log("click here")
    window.location.href = '/property'
    return true;
  }

  async query_searchProperty(event){
    event.preventDefault();
    let data = getDataForm(event.target);
    
    let response = await this.property_obj.get_searchProperty(data.city, data.inicio, data.fim, data.guests)
    console.log(response)

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
            {json.items.map((item, key) => (
              <div class="col-lg-4" key={key} onClick={() => this.property_info()} >
                <GalleryCard >
                  <GalleryCard.Image
                    src={item.imageURL}
                    alt={`Photo by ${item.fullName}`}
                    onClick={() => this.property_info()} 
                  />
                  <GalleryCard.Footer>
                    <GalleryCard.Details
                      avatarURL={item.avatarURL}
                      fullName={item.fullName}
                      dateString={item.dateString}
                    />
                    <GalleryCard.IconGroup>
                      <Icon prefix="fa" name="male"  />  <span style={{padding : '5px'}}>{item.totalRooms}</span>
                      <Icon prefix="fa" name="bed"  />  <span style={{padding : '5px'}}>{item.totalRooms}</span>
                      <Icon prefix="fa" name="bath" />  <span style={{paddingLeft : '5px'}}>{item.totalWC}</span>
                    </GalleryCard.IconGroup>
                  </GalleryCard.Footer>
                  <span style={{paddingLeft:"300px", fontSize:"25px" }}>{item.price}€</span>
                </GalleryCard>
              </div>
            ))}
          </Grid.Row>
        </Page.Content>
      </SiteWrapper>
    );
  }
  
}



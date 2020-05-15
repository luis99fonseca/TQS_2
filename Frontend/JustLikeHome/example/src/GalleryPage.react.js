// @flow

import React, { Component, useCallback } from "react";
import { Page, Grid, GalleryCard, Form, Button} from "tabler-react";
import Icon from "./components/Icon";
import "tabler-react/dist/Tabler.css";
import SiteWrapper from "./SiteWrapper.react";

import json from "./data/Gallery.Items";


export default class GalleryPage extends Component {

  constructor(props) {
      super(props);

      this.property_info = this.property_info.bind(this);
  }

  property_info() {
    //localStorage.setItem('property_id', id);
    console.log("click here")
    window.location.href = '/property'
    return true;
  }


  render(){
    const options = (
      <React.Fragment>
        <Form>
          <div class="row">
            <div class="col-lg-4">
            <form class="group" >
              <input  placeholder="Insira localização" style={{marginBottom:"30px", marginTop:"30px"}} required/>
              <input type="number"  min="0" placeholder="Número de pessoas" required/>
            </form>
            </div>
          
            <Form.Group>
              <Form.Label>Data de Início:</Form.Label>
  
              <Form.DatePicker
                   defaultDate={new Date("2020-05-13T16:06:07.669Z")}
                   format="mm/dd/yyyy"
                   required
                   maxYear={2030}
                   minYear={2020}
                   monthLabels={[
                     'Janeiro',
                     'Fevereiro',
                     'Março',
                     'Abril',
                     'Maio',
                     'Junho',
                     'Julho',
                     'Agosto',
                     'Setembro',
                     'Outubro',
                     'Novembro',
                     'Dezembro'
                   ]}
                 />
                 <Form.Label>Data de Fim:</Form.Label>
                 <Form.DatePicker
                    defaultDate={new Date("2020-05-13T16:06:07.669Z")}
                    format="mm/dd/yyyy"
                    required
                    maxYear={2030}
                    minYear={2020}
                    monthLabels={[
                      'Janeiro',
                      'Fevereiro',
                      'Março',
                      'Abril',
                      'Maio',
                      'Junho',
                      'Julho',
                      'Agosto',
                      'Setembro',
                      'Outubro',
                      'Novembro',
                      'Dezembro'
                    ]}
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



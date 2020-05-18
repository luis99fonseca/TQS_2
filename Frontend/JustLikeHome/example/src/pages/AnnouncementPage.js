import React, { Component, useCallback } from "react";
import {  url } from "../Rest/Const";
import SiteWrapper from "../SiteWrapper.react";
import { Page, Grid, Form, Table, Card, Icon, Button } from "tabler-react";

import House from "../Rest/House"
import getDataForm from "../Rest/getDataForm";

export default class AnnoucementPage extends Component{

    constructor(props) {
        super(props);
        
        this.state = {
            startDate : new Date(),
            endDate : new Date(),
            houses : [
              {id: -4,  rating:0 ,ownerName:"" , city:"", description:"", kmFromCityCenter:0, pricePerNight:0.0, numberOfBeds: 0, maxNumberOfUsers:0, comodities: []}
            ]
          }

          this.house_obj = new House()
          this.get_userHouses()
          this.create_house = this.create_house.bind(this)
  
    }

    async get_userHouses(){

      let response = await this.house_obj.get_userHouse()
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

    async create_house(event){
        event.preventDefault();
        let data = getDataForm(event.target);
        console.log(data)
        let extras = ["Piscina", "Spa", "Campo de futebol", "Ginásio"]
        let comodities = []
        for (var param in data){
          if(extras.includes(param)){
            comodities.push(param)
            delete data[param]
          }   
        }
        data["userId"] = 1 // DEFAULT, DPS E PRECISO IR BUSCAR A CACHE O ID DO USER LOGADO
        // console.log(data)
        let response = await this.house_obj.post_house(data)
        let status = response[0]
        let houses = response[1]
        
        this.get_userHouses()
    }

    render(){
        return(
            <SiteWrapper>
                <Page.Content title="Seus Anúncios">
                    <Grid.Row>
                        <Grid.Col lg={3}>
                          <Form onSubmit={this.create_house}>
                          <Form.Group
                                isRequired
                                label="Título do anúncio"
                              >
                                <Form.Input required  name="houseName" />
                              </Form.Group>
                            <Form.Group
                                isRequired
                                label="Cidade"
                              >
                                <Form.Input required  name="city" />
                              </Form.Group>
                              <Form.Group
                                isRequired
                                label="Distância da cidade centro(km)"
                              >
                                <Form.Input type="number" required min="0" name="kmFromCityCenter" />
                              </Form.Group>
                              <Form.Group
                                isRequired
                                label="Quartos"
                              >
                                <Form.Input type="number"  required min="0" name="numberOfBeds" />
                              </Form.Group>
                              <Form.Group
                                isRequired
                                label="Limite de pessoas disponíveis"
                              >
                                <Form.Input type="number" min="0" required name="maxNumberOfUsers" />
                              </Form.Group>
                              <Form.Group
                                className="mb-0"
                                label="Descrição"
                              >
                                <Form.Textarea required name="description" />
                              </Form.Group>
                              <Form.Group label="Extras" name="comedities">
                                    <Form.Checkbox
                                      label="Piscina"
                                      name="Piscina"
                                      value="Piscina"
                                    />
                                    <Form.Checkbox
                                      label="Ginásio"
                                      name="Ginásio"
                                      value="Ginásio"
                                    />
                                    <Form.Checkbox
                                      label="Spa"
                                      name="Spa"
                                      value="Spa"
                                    />
                                    <Form.Checkbox
                                      label="Campo de futebol"
                                      name="Campo de futebol"
                                      value="Campo de futebol"
                                    />
                                </Form.Group>
                              <Form.Group
                                className="mb-0"
                                label="Preço por noite (€)"
                              >
                                <Form.Input required type="number" min="0" name="pricePerNight" />
                              </Form.Group>
                              <div style={{paddingTop:"20px"}}></div>
                              <Button type="submit" color="primary">Criar</Button>
                          </Form>
                        </Grid.Col>
                        <Grid.Col lg={9}>
                            <Card>
                                <Table className="card-table table-vcenter">
                                    <Table.Body>
                                    {this.state.houses.map((house) => (
                                        <Table.Row>
                                            <Table.Col>
                                            <img
                                              alt=""
                                              src={"demo/photos/apart_example.jpg"}
                                              className="h-8"
                                            />
                                            </Table.Col>
                                            <Table.Col>
                                                {house.houseName}
                                            </Table.Col>
                                            <Table.Col className="text-right text-muted d-none d-md-table-cell text-nowrap">
                                                <Icon prefix="fa" name="male"  />  <span style={{padding : '5px'}}>{house.maxNumberOfUsers}</span>
                                            </Table.Col>
                                            <Table.Col className="text-right text-muted d-none d-md-table-cell text-nowrap">
                                                <Icon prefix="fa" name="bed"  />  <span style={{padding : '5px'}}>{house.numberOfBeds}</span>
                                            </Table.Col>
                                            <Table.Col className="text-right">
                                                <strong>{house.pricePerNight}€</strong> /por noite
                                            </Table.Col>
                                        </Table.Row>
                                    )
                                    )}
                                    </Table.Body>
                                </Table>
                            </Card>
                        </Grid.Col>
                    </Grid.Row>
                </Page.Content> 
            </SiteWrapper>
        )
    }
}
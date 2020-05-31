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
            update : false,
            startDate : new Date(),
            endDate : new Date(),
            houseId: -4,  rating:0, houseName: "", userId:"" , city:"", description:"", kmFromCityCenter:0, pricePerNight:0.0, numberOfBeds: 0, maxNumberOfUsers:0, comodities: [],
            houses : [
              {id: -4,  rating:0 ,ownerName:"" , city:"", description:"", kmFromCityCenter:0, pricePerNight:0.0, numberOfBeds: 0, maxNumberOfUsers:0, comodities: []}
            ]
          }

          this.house_obj = new House()

          this.check_login()
          this.get_userHouses()
          this.create_house = this.create_house.bind(this)
          this.render_update = this.render_update.bind(this)
          this.choice_house_update = this.choice_house_update.bind(this)
          this.updateProperty = this.updateProperty.bind(this)
    }

    check_login(){
      console.log(localStorage.getItem("user_id"))
      if (localStorage.getItem("user_id") === null || localStorage.getItem("user_id") === "" ){
        window.location.href = '/login'
      }
  
    }

    async get_userHouses(){

      let response = await this.house_obj.get_userHouse()
      let status = response[0]
      let houses_data = response[1]
      let state_houses = []
      houses_data.map((home) => (
        state_houses.push(home)
      )
      )
      
      this.setState({
        update : false,
        houses : state_houses
      })
    }

    async create_house(event){
        event.preventDefault();
        let data = getDataForm(event.target);
       
        let extras = ["Piscina", "Spa", "Campo de futebol", "Ginásio"]
        let comodities = []
        for (var param in data){
          if(extras.includes(param)){
            comodities.push({"type": param, "description": ""})
            delete data[param]
          }   
        }
        
        data["userId"] = localStorage.getItem('user_id')
        data["comodities"] = comodities
        await this.house_obj.post_house(data)

        this.get_userHouses()
    }

    render_update = () => {
      return(
        <Button color="info"  type="button" onClick={this.updateProperty}>Atualizar</Button>
      )
    }

    choice_house_update(house){

      this.setState({
        update: true,
        houseId: house.id,
        comodities: house.comodities,
        numberOfBeds: house.numberOfBeds,
        houseName : house.houseName,
        city: house.city,
        kmFromCityCenter : house.kmFromCityCenter,
        maxNumberOfUsers : house.maxNumberOfUsers,
        description: house.description,
        pricePerNight: house.pricePerNight
      })
    }

    async updateProperty(){
        let data = {
          houseId: this.state.houseId,
          numberOfBeds: this.state.numberOfBeds,
          houseName : this.state.houseName,
          city: this.state.city,
          kmFromCityCenter : this.state.kmFromCityCenter,
          maxNumberOfUsers : this.state.maxNumberOfUsers,
          description: this.state.description,
          pricePerNight: this.state.pricePerNight
        }
        let response = await this.house_obj.updateHouse(data)
        let msg = response[1]

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
                                <Form.Input required  name="houseName" value={this.state.houseName} onChange={(e) => this.setState({houseName: e.target.value})}  />
                              </Form.Group>
                            <Form.Group
                                isRequired
                                label="Cidade"
                              >
                                <Form.Input required  name="city" value={this.state.city} onChange={(e) => this.setState({city: e.target.value})} />
                              </Form.Group>
                              <Form.Group
                                isRequired
                                label="Distância da cidade centro(km)"
                              >
                                <Form.Input type="number" required min="0" name="kmFromCityCenter" value={this.state.kmFromCityCenter} onChange={(e) => this.setState({kmFromCityCenter: e.target.value})}  />
                              </Form.Group>
                              <Form.Group
                                isRequired
                                label="Quartos"
                              >
                                <Form.Input type="number"  required min="0" name="numberOfBeds" value={this.state.numberOfBeds} onChange={(e) => this.setState({numberOfBeds: e.target.value})} />
                              </Form.Group>
                              <Form.Group
                                isRequired
                                label="Limite de pessoas disponíveis"
                              >
                                <Form.Input type="number" min="0" required name="maxNumberOfUsers" value={this.state.maxNumberOfUsers} onChange={(e) => this.setState({maxNumberOfUsers: e.target.value})}  />
                              </Form.Group>
                              <Form.Group
                                className="mb-0"
                                label="Descrição"
                              >
                              <Form.Textarea required name="description" value={this.state.description} onChange={(e) => this.setState({description: e.target.value})} />
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
                                isRequired
                                label="Preço por noite (€)"
                              >
                                <Form.Input required type="number" min="0" name="pricePerNight"  value={this.state.pricePerNight} onChange={(e) => this.setState({pricePerNight: e.target.value})}/>
                              </Form.Group>  
                              <div style={{paddingTop:"20px"}}></div>
                              <Button color="primary" type="submit">Criar</Button> <span style={{marginRight:"5px"}}></span>
                              {this.state.update === true && this.render_update()}
                          </Form>
                        </Grid.Col>
                        <Grid.Col lg={9}>
                            <Card>
                                <Table className="card-table table-vcenter">
                                    <Table.Body>
                                    {this.state.houses.map((house) => (
                                        <Table.Row onClick={() => this.choice_house_update(house)}>
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
import React, { Component, useCallback } from "react";
import {  url } from "../Rest/Const";
import SiteWrapper from "../SiteWrapper.react";
import { Page, Grid, Form, Table, Card, Icon, Button } from "tabler-react";

import User from "../Rest/User"
import getDataForm from "../Rest/getDataForm";

export default class RentsPage extends Component {


    constructor(props) {
        super(props);
        
        this.state = {
            rents_requests : [],
            onGoing_rents : []
          }

          this.user_obj = new User()

          this.get_rentsRequests = this.get_rentsRequests.bind(this)
          this.get_onGoingRents = this.get_onGoingRents.bind(this)
          this.go_profileUser = this.go_profileUser.bind(this)
          this.get_rentsRequests()
          

          this.accept_rents = this.accept_rents.bind(this)
    }


    async get_rentsRequests(){
        let response = await this.user_obj.get_rents_requests()
        let status = response[0]
        let rentsRequests = response[1]
        let rents = []

        rentsRequests.map((askRent) => (
          rents.push(askRent)
        ))

        this.setState({
            rents_requests: rents
        })

        this.get_onGoingRents()
    }

    async get_onGoingRents(){
        let response = await this.user_obj.get_rents_ongoing()
        let status = response[0]
        let onGoingRents = response[1]
        let ogrents = []

        onGoingRents.map((og) => (
          ogrents.push(og)
        ))

        this.setState({
            onGoing_rents: onGoingRents
        })
    }

    async accept_rents(id){
        let data = {"rentID":id}
        let response = await this.user_obj.accept_rents(data)
        let status = response[0]
        
        this.get_rentsRequests()
    }

   
    go_profileUser(id){
        localStorage.setItem('client_id', id)
        window.location.href = '/user/profile'
        return true;
    }

    render(){
        return(
            <SiteWrapper>
                <Page.Content title="Arrendamentos">
                    <Grid.Row>
                        <Grid.Col lg={12}>
                            <Card title="Pedidos">
                                <Table className="card-table table-vcenter">
                                    <Table.Body>
                                    {this.state.rents_requests.map((rent) => (
                                    <Table.Row>
                                        <Table.Col>
                                            <span>{rent['user'].firstName} {rent['user'].lastName}</span> <div></div>
                                            <span>{rent['user'].username}</span>
                                        </Table.Col>
                                        <Table.Col className="text-right text-muted d-none d-md-table-cell text-nowrap">
                                            <Icon prefix="fe" name="arrow-right"  /> 
                                        </Table.Col>
                                        <Table.Col> 
                                            {rent['house'].houseName}
                                        </Table.Col>
                                        <Table.Col className="text-right text-muted d-none d-md-table-cell text-nowrap">
                                            <span> A partir de {rent.rentStart} a {rent.rentEnd}</span>
                                        </Table.Col>
                                        <Table.Col className="text-right">
                                            <Button color="success" icon="check" onClick={() => this.accept_rents(rent.id)} />
                                            <span style={{padding:"5px"}}></span>
                                            <Button color="danger" icon="trash"  />
                                        </Table.Col>
                                    </Table.Row>
                                )
                                )}
                                    </Table.Body>
                                </Table>
                            </Card>

                            <Card title="Em progresso/Concluídos">
                                <Table className="card-table table-vcenter">
                                    <Table.Body>
                                    {this.state.onGoing_rents.map((rent) => (
                                    <Table.Row>
                                        <Table.Col>
                                            <span onClick={() => this.go_profileUser(rent['user'].id)}>
                                                {rent['user'].firstName} {rent['user'].lastName}
                                            </span> <div></div>
                                            <span onClick={() => this.go_profileUser(rent['user'].id)} >{rent['user'].username}</span>
                                        </Table.Col>
                                        <Table.Col className="text-right text-muted d-none d-md-table-cell text-nowrap">
                                            <Icon prefix="fe" name="arrow-right"  /> 
                                        </Table.Col>
                                        <Table.Col> 
                                            {rent['house'].houseName}
                                        </Table.Col>
                                        <Table.Col className="text-right text-muted d-none d-md-table-cell text-nowrap">
                                            <span> De {rent.rentStart} a {rent.rentEnd}</span>
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
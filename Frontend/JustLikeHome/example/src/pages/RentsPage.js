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
          this.get_rentsRequests()
          this.accept_rents = this.accept_rents.bind(this)
      //    this.get_onGoingRequests()
    }


    async get_rentsRequests(){
        console.log("a tentar pegar rents")
        let response = await this.user_obj.get_rents_requests()
        let status = response[0]
        let rentsRequests = response[1]
        let rents = []

        rentsRequests.map((askRent) => (
          rents.push(askRent)
        ))

        rents.map((r)=> (
            console.log(r['house'].houseName)
        ))

        this.setState({
            rents_requests: rents
        })
    }

    async accept_rents(id){
            console.log(id)
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
                                            <Button color="success" icon="check" />
                                            <span style={{padding:"5px"}}></span>
                                            <Button color="danger" icon="trash" onClick />
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
import React, { Component, useCallback } from "react";
import {  url } from "./Const";
import SiteWrapper from "../SiteWrapper.react";
import { Page, Grid, StoreCard, Table, Card, Badge, Button } from "tabler-react";

export default class Annoucement extends Component{

    constructor(props) {
        super(props);
        
        this.state = {
            startDate : new Date(),
            endDate : new Date(),
            houses : [
              {id: -2,  city:"Aveiro", description:"Casa muito humilde com tudo o que precisa!", kmFromCityCenter:3, pricePerNight:300.0, numberOfBeds: 1, maxNumberOfUsers:1, comodities: []},
              {id: -3,  city:"Porto", description:"Casa muito humilde com tudo o que precisa!", kmFromCityCenter:3, pricePerNight:200.0, numberOfBeds: 2, maxNumberOfUsers:1, comodities: []},
              {id: -4,  city:"Lisboa", description:"Casa muito humilde com tudo o que precisa!", kmFromCityCenter:3, pricePerNight:500.0, numberOfBeds: 3, maxNumberOfUsers:1, comodities: []}
            ]
          }
    }


    render(){
        return(
            <SiteWrapper>
                <Page.Content title="Store Components">
                    <Grid.Row>
                        <Grid.Col lg={3}>
                          <form>
                              <input placeholder="Insira cidade"/>
                              <Button type="submit" color="secondary">Adicionar</Button>
                          </form>
                        </Grid.Col>
                        <Grid.Col lg={9}>
                            <Card>
                                <Table className="card-table table-vcenter">
                                    <Table.Body>
                                    <Table.Col>
                                        <img
                                          alt=""
                                          src="https://tabler.github.io/tabler/demo/products/apple-iphone7-special.jpg"
                                          className="h-8"
                                        />
                                    </Table.Col>
                                    <Table.Col>
                                        Apple iPhone 7 Plus 256GB Red Special Edition
                                    </Table.Col>
                                    <Table.Col className="text-right text-muted d-none d-md-table-cell text-nowrap">
                                           98 reviews
                                    </Table.Col>
                                    <Table.Col className="text-right text-muted d-none d-md-table-cell text-nowrap">
                                        38 offers
                                    </Table.Col>
                                    <Table.Col className="text-right">
                                        <strong>$499</strong>
                                    </Table.Col>
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
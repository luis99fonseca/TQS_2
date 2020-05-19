// https://www.npmjs.com/package/react-simple-image-slider
import SimpleImageSlider from "react-simple-image-slider";

import React, { Component, useCallback } from "react";
import { Page, Grid, GalleryCard, Form, Button, Container, Text, Card} from "tabler-react";
import SiteWrapper from "./SiteWrapper.react";
import House from "./Rest/House"
import getDataForm from "./Rest/getDataForm"

import DatePicker from "react-datepicker";

import Rating from "react-rating";
 
import "react-datepicker/dist/react-datepicker.css";

export default class Property extends Component {
    constructor(props) {
        super(props);
    
        this.state = {
            startDate : new Date(),
            endDate : new Date(),
            house: {
                city: "",
                description: "",
                houseName: "",
                kmFromCityCenter: 0,
                pricePerNight: 0.0,
                numberOfBeds: 0,
                maxNumberOfUsers: 0,
                rating: 0.0,
                userRating: 0.0,
                ownerName: ""
            },
            reviews:[]
        }

        this.house_obj = new House()
        this.get_house()
    
    }

    async get_house(){
    let response = await this.house_obj.get_specificHouse()
    // let status = response[0]
    let house_rcv = response[1]

    let response_review = await this.house_obj.get_reviews()
    let reviews_rcv = response_review[1]

    this.setState({
        house : house_rcv,
        reviews: reviews_rcv
      })
    }

    handleChange(date, name){
        this.setState({
          [name]: date
        })
    
      };

    async ask_rent(event){
        event.preventDefault();
        let data = getDataForm(event.target);
        console.log(data)
    }

    render() {
        const images = [
            { url: "demo/photos/example1-apart.jpg" },
            { url: "demo/photos/example2-apart.jpg" },
            { url: "demo/photos/example3-apart.jpg" },
        ];
        const avatarUrl = "demo/faces/female/7.jpg"
        //const listComedities = comedities.map((c) => <li>{c}</li>)

        return(
            <SiteWrapper>
                <Container>
                    <div>
                        <SimpleImageSlider
                            width={896}
                            height={504}
                            images={images}
                            style={{marginRight:"auto", marginLeft:"auto"}}
                        />
                    </div>
            
            <h1 style={{fontSize:"70px", marginTop:"50px", marginBottom:"0px"}}>{this.state.house.houseName}</h1>
            <Rating 
                initialRating={this.state.house.rating} 
                readonly
                emptySymbol="fa fa-star-o fa-2x"
                fullSymbol="fa fa-star fa-2x"
                fractions={2}
             />
            
            

            <div style={{ borderBottom:"1px solid", marginTop:"50px"}}>
                <h2>Propriedades</h2>
                <div class="row">
                    <div class="col-lg-6">
                        <p>Cidade: {this.state.house.city}</p>
                        <p>Distância à cidade centro: {this.state.house.kmFromCityCenter}</p> 
                    </div>
                    <div class="col-lg-6">
                        <p>Número de pessoas disponíveis: {this.state.house.maxNumberOfUsers} </p>
                        <p>Número de quartos: {this.state.house.numberOfBeds} </p>
                    </div>
                </div>
            </div>
            <div style={{ borderBottom:"1px solid", marginTop:"50px"}}>
                <h2>Descrição</h2>
                <p>{this.state.house.description}</p>
            </div>
            <div style={{ borderBottom:"1px solid",marginTop:"50px"}}>
                <h2>Características</h2>
                <ul>{"Nenhuma"}</ul>
            </div>
            <div style={{ borderBottom:"1px solid",marginTop:"50px"}} class="row">
                <div class="col-lg-12">
                    <h2>Últimas Reviews:</h2>
                </div>
                {this.state.reviews.map((rev, index)=>{
                    console.log(index)
                    return(
                    <div class="col-lg-6">
                        <Card key={rev.user.username}>
                            <Card.Header>
                              <Card.Title>{rev.user.firstName + " " +rev.user.lastName}</Card.Title>
                            </Card.Header>
                            <Card.Body>
                              {rev.description}
                            </Card.Body>
                            <Card.Footer><b>Avaliação:</b>  {rev.rating}</Card.Footer>
                        </Card>   
                    </div>
                    )
                })}
            </div>
            <form onSubmit={this.ask_rent}>
                <div class="row" style={{marginTop:"50px"}}>
                <div class="col-lg-2">
                    <GalleryCard.Details
                        avatarURL={avatarUrl}
                        fullName={this.state.house.ownerName}
                    />
                    <span>Rating do proprietário: {this.state.house.userRating}</span>
                </div>
               
                    <div class="col-lg-3">
                        <p>Data de Início:</p>
                        <DatePicker
                          name="rentStart"
                           selected={this.state.startDate}
                           dateFormat="dd-MM-yyyy"
                           onChange={(date) => this.handleChange(date, "startDate")}
                         />
                    </div>
                    <div class="col-lg-3">
                        <p>Data de Fim:</p>
                        <DatePicker
                          name="rentEnd"
                          selected={this.state.endDate}
                          dateFormat="dd-MM-yyyy"
                          min={this.state.startDate}
                          onChange={(date) => this.handleChange(date, "endDate")}
                        />
                    </div>
                    <div class="col-lg-4">
                        <h3 style={{fontSize:"20px"}}><b style={{fontSize:"40px"}}>{this.state.house.pricePerNight}€</b>/por noite</h3>
                        <button type="button" class="btn btn-primary" style={{size:"lg"}} >Alugar</button>
                    </div>
               
                </div>
            </form>

                    
                </Container>
            </SiteWrapper>
            

        );
    }

}
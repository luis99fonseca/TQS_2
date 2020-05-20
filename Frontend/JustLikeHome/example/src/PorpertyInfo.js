// https://www.npmjs.com/package/react-simple-image-slider
import SimpleImageSlider from "react-simple-image-slider";

import React, { Component, useCallback } from "react";
import { Page, Grid, GalleryCard, Form, Button, Container, Text, Card} from "tabler-react";
import SiteWrapper from "./SiteWrapper.react";
import House from "./Rest/House"
import User from "./Rest/User"
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
            review_rating: 0,
            msg_error: [false, "Nunca esteve em nenhum dos seus bens imóveis"],
            form_review : false,
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
            reviews:[],
            feedback_askrent: "Pedido feito com sucesso",
            pending: false
        }

        this.user_obj = new User()
        this.house_obj = new House()

        this.ask_rent = this.ask_rent.bind(this)
        this.renderFeedbackRent = this.renderFeedbackRent.bind(this)
        this.render_formReview = this.render_formReview.bind(this)
        this.my_button = this.my_button.bind(this)
        this.change_rating = this.change_rating.bind(this)
        this.review_house = this.review_house.bind(this)

        this.get_house()
    }


    async ask_rent(event){
        event.preventDefault();
        let data = getDataForm(event.target);
        data["userID"] = "1" // later use cache for id user
        data["houseID"] = localStorage.getItem('house_id')

        console.log(data)
        let response = await this.user_obj.askRent_house(data)
        let status = response[0]
        let check_pending = response[1]

        this.setState({
            pending : check_pending['pending']
        })
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

    async review_house(event){
        event.preventDefault();
        let data = getDataForm(event.target);
        
        data['reviewerId'] = 1 //use cache later
        data['houseId'] = localStorage.getItem('house_id')
        data['rating'] = this.state.review_rating
        let response = await this.house_obj.makeReview_house(data)
        let status = response[0]    
    
        this.setState({
            form_review: false
        })
    
        if(status === 401){
            this.setState({
                msg_error: [true, "Nunca esteve em nenhum dos seus bens imóveis"]
            })
        }else{
            this.get_house()
        }
        
      }

    handleChange(date, name){
        this.setState({
          [name]: date
        })
    
      }

    renderFeedbackRent() {
        return (
        <span style={{color: "green", marginLeft:"10px"}}>{this.state.feedback_askrent}</span>
        )
      }
    
    change_rating(value){
        this.setState({
            review_rating: value
        })
    }
  
    rend_error(){
        return(
        <span style={{color:"red", marginLeft:"5px"}}>{this.state.msg_error[1]}</span>
        )
    }
  
    my_button =() => {
        return(
          <Button icon="plus" color="success" onClick={() => this.setState({form_review: true, msg_error: [false, "Nunca esteve em nenhum dos seus bens imóveis"] }) }/>
  
        )
    }

    render_formReview = () => {
        return(
          
                  <Form onSubmit={this.review_house}>
                  <Form.Group
                      className="mb-0"
                      label="Comentário"
                    >
                <Form.Textarea required name="description" />
                  </Form.Group>
                  <Form.Group label="Avaliação">
                      <Rating
                          initialRating={this.state.review_rating}
                          onChange={(value)=>this.change_rating(value)}
                          name="rating" 
                          emptySymbol="fa fa-star-o fa-2x"
                          fullSymbol="fa fa-star fa-2x"
                          fractions={2}
                       />
                  </Form.Group>
  
                  <Button type="submit" color="success">Criar Review</Button>
                  </Form>
          
                
        )
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
                {this.state.form_review ? this.render_formReview() : this.my_button()}
                {this.state.msg_error[0] === true && this.rend_error()}
            </div>
            <Form onSubmit={this.ask_rent}>
                <div class="row" style={{marginTop:"60px"}}>
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
                          name="startDate"
                           selected={this.state.startDate}
                           dateFormat="dd-MM-yyyy"
                           onChange={(date) => this.handleChange(date, "startDate")}
                         />
                    </div>
                    <div class="col-lg-3">
                        <p>Data de Fim:</p>
                        <DatePicker
                          name="endDate"
                          selected={this.state.endDate}
                          dateFormat="dd-MM-yyyy"
                          min={this.state.startDate}
                          onChange={(date) => this.handleChange(date, "endDate")}
                        />
                    </div>
                    <div class="col-lg-4">
                        <h3 style={{fontSize:"20px"}}><b style={{fontSize:"40px"}}>{this.state.house.pricePerNight}€</b>/por noite</h3>
                        <Button type="submit" color="primary">Alugar</Button>
                        {this.state.pending === true && this.renderFeedbackRent()}
                    </div>
               
                </div>
            </Form>

                    
                </Container>
            </SiteWrapper>
            

        );
    }

}
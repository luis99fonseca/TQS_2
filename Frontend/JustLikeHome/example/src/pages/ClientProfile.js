import React, { Component, useCallback } from "react";

import {
  Container,
  Grid,
  Card,
  Button,
  Form,
  Avatar,
  Profile,
  List,
  Media,
  Text,
  Comment,
} from "tabler-react";
import User from "../Rest/User"
import SiteWrapper from "../SiteWrapper.react";
import Rating from "react-rating";
import CardTitle from "../components/Card/CardTitle.react";
import getDataForm from "../Rest/getDataForm"

export default class ClientProfile extends Component {
  constructor(props) {
      super(props);

      this.state= {
          rating: 0,
          form_review : false,
          msg_error: [false, "Nunca esteve em nenhum dos seus bens imóveis"],
          user_id: localStorage.getItem('client_id'),
          reviews_user : []
      }

      this.user_obj = new User()
      this.get_userReviews = this.get_userReviews.bind(this)
      this.review_user = this.review_user.bind(this)
      this.render_formReview = this.render_formReview.bind(this)
      
      this.my_button = this.my_button.bind(this)
      
      this.get_userReviews()
  }

  async get_userReviews(){
    let response = await this.user_obj.get_user_reviews(this.state.user_id)
    let data = response[1]
    let reviews = []
    data.map((i) => (
      reviews.push(i)
    ))

    this.setState({
        reviews_user: reviews
    })    
  }


  async review_user(event){
    event.preventDefault();
    let data = getDataForm(event.target);
    data['reviewerId'] = 1 //use cache later
    data['reviewedId'] = localStorage.getItem('client_id')
    data['rating'] = this.state.rating
    let response = await this.user_obj.makeReview_otherUser(data)
    let status = response[0]    

    this.setState({
        form_review: false
    })

    if(status === 401){
        this.setState({
            msg_error: [true, "Nunca esteve em nenhum dos seus bens imóveis"]
        })
    }else{
        this.get_userReviews()
    }
    
  }

  change_rating(value){
      this.setState({
          rating: value
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
        
                <Form onSubmit={this.review_user}>
                <Form.Group
                    className="mb-0"
                    label="Comentário"
                  >
                    <Form.Textarea required name="description" />
                </Form.Group>
                <Form.Group label="Avaliação">
                    <Rating
                        initialRating={this.state.rating}
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
  const username = "andryz"
  const firstName = "André"
  const lastName = "Baião"
  const name = firstName + " " + lastName
  const birthDate = "01/07/1999"

  return (
    <SiteWrapper>
      <div className="my-3 my-md-5">
        <Container>
          <Grid.Row>
            <Grid.Col lg={4}>
              <Profile
                name={name}
                backgroundURL={"/demo/photos/eberhard-grossgasteiger-311213-500.jpg"}
                avatarURL="/demo/faces/male/16.jpg"
              >
              <p>{username}</p>
              <p>{birthDate}</p>
              </Profile>
            </Grid.Col>
            <Grid.Col lg={8}>
              <Card>
                <Card.Header>
                  <CardTitle>Últimas reviews feitas a {username}</CardTitle>                    
                </Card.Header>
                <Comment.List>
                  {this.state.reviews_user.map((review, index)=> (
                      <Comment
                      key={index}
                      avatarURL="/demo/faces/male/15.jpg"
                      name={`${review['userReviewing'].firstName}  ${review['userReviewing'].lastName}`}
                      date={`Avaliação: ${review.rating}`} 
                      text={review.description}
                    />
                  ))}
                </Comment.List>
                
              </Card>
              
            {this.state.form_review ? this.render_formReview() : this.my_button()}
            {this.state.msg_error[0] === true && this.rend_error()}
            </Grid.Col>
          </Grid.Row>
        </Container>
      </div>
    </SiteWrapper>
  );
}}

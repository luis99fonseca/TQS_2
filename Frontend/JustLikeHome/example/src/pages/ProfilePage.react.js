// @flow

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
  Icon,
  Comment,
  Table
} from "tabler-react";
import Rating from "react-rating";
import User from "../Rest/User"
import SiteWrapper from "../SiteWrapper.react";
import CardTitle from "../components/Card/CardTitle.react";

export default class ProfilePage extends Component {
  constructor(props) {
      super(props);

      this.state= {
          user_id : 1,
          reviews_user : [],
          user: {
            bookmarkedHouses: [],
            purchasedRents : []
          }
      }

      this.user_obj = new User()
      this.get_userReviews = this.get_userReviews.bind(this)
      this.get_info = this.get_info.bind(this)
      this.get_deny_bookmarker = this.get_deny_bookmarker.bind(this)
      this.property_info = this.property_info.bind(this)

      this.get_userReviews()
      this.get_info()
  }

  async get_userReviews(){
    let response = await this.user_obj.get_user_reviews(this.state.user_id)
    let status = response[0]
    let data = response[1]
    let reviews = []
    data.map((i) => (
      reviews.push(i)
    ))

    this.setState({
        reviews_user: reviews
    })    
  }


  async get_info(){
    let response = await this.user_obj.getInfoUser()
    let status = response[0]
    let data = response[1]

    this.setState({
        user: data
    })
  }

  property_info(id) {
    localStorage.setItem('house_id', id);
    window.location.href = '/property'
    return true;
  }


  async get_deny_bookmarker(id){
    await this.user_obj.deleteBookMarker(id)
    this.get_info()
  }


  render() {

  return (
    <SiteWrapper>
      <div className="my-3 my-md-5">
        <Container>
          <Grid.Row>
            <Grid.Col lg={4}>
              <Profile
                name={`${this.state.user.firstName} ${this.state.user.lastName}`}
                backgroundURL="demo/photos/eberhard-grossgasteiger-311213-500.jpg"
                avatarURL="demo/faces/male/16.jpg"
              >
              <p>{this.state.user.username}</p>
              <p>{this.state.user.birthDate}</p>
              <Rating 
                initialRating={this.state.user.rating} 
                readonly
                emptySymbol="fa fa-star-o fa-2x"
                fullSymbol="fa fa-star fa-2x"
                fractions={2}
             />
              </Profile>
            </Grid.Col>
            <Grid.Col lg={8}>
              <Card>
                <Card.Header>
                  <CardTitle>Últimas reviews feitas a si</CardTitle>                    
                </Card.Header>
                <Comment.List>
                  {this.state.reviews_user.map((review)=> (
                      <Comment
                      avatarURL="demo/faces/male/16.jpg"
                      name={`${review['userReviewing'].firstName}  ${review['userReviewing'].lastName}`}
                      date={`Avaliação: ${review.rating}`} 
                      text={review.description}
                    />
                  ))}
                </Comment.List>
              </Card>
              <Card
                title="Favoritos"
              >
                <Table className="card-table table-vcenter">
                  <Table.Body>
                    {this.state.user.bookmarkedHouses.map((rent)=>(
                      <Table.Row >
                        <Table.Col>
                        <img
                          alt=""
                          src={"demo/photos/apart_example.jpg"}
                          className="h-8"
                        />
                        </Table.Col>
                        <Table.Col>
                          {rent.houseName}
                        </Table.Col>
                        <Table.Col>
                          {rent.city}
                        </Table.Col>
                        <Table.Col>
                          <strong>{rent.pricePerNight}€</strong> /por noite
                        </Table.Col>
                        <Table.Col>
                          <Button color="secondary"  onClick={() => this.property_info(rent.id)}><Icon prefix="fa" name="eye" /></Button>
                            <span style={{marginLeft:"5px"}}></span>
                          <Button color="danger" icon="trash" onClick={() => this.get_deny_bookmarker(rent.id)} />
                        </Table.Col>
                      </Table.Row>
                    ))}
                  </Table.Body>
                </Table>
              </Card>

              <Card
                title="Suas ultímas experiências"
              >
                <Table className="card-table table-vcenter">
                  <Table.Body>
                    {this.state.user.purchasedRents.map((rent)=>(
                      <Table.Row >
                        <Table.Col>
                        <img
                          alt=""
                          src={"demo/photos/apart_example.jpg"}
                          className="h-8"
                        />
                        </Table.Col>
                        <Table.Col>
                          {rent.house.houseName}
                        </Table.Col>
                        <Table.Col>
                        <span> De {rent.rentStart} a {rent.rentEnd}</span>
                        </Table.Col>
            
                        <Table.Col>
                          <Button color="secondary"  onClick={() => this.property_info(rent.id)}><Icon prefix="fa" name="eye" /></Button>
                        </Table.Col>
                      </Table.Row>
                    ))}
                  </Table.Body>
                </Table>
              </Card>
            </Grid.Col>
          </Grid.Row>
        </Container>
      </div>
    </SiteWrapper>
  );
}}


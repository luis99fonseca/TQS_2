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
  Comment,
  Table
} from "tabler-react";
import User from "../Rest/User"
import SiteWrapper from "../SiteWrapper.react";
import CardTitle from "../components/Card/CardTitle.react";

export default class ProfilePage extends Component {
  constructor(props) {
      super(props);

      this.state= {
          user_id : 1,
          reviews_user : []
      }

      this.user_obj = new User()
      this.get_userReviews = this.get_userReviews.bind(this)

      this.get_userReviews()
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
                backgroundURL="demo/photos/eberhard-grossgasteiger-311213-500.jpg"
                avatarURL="demo/faces/male/16.jpg"
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
                      <Table.Row>
                          <Table.Col>
                          <img
                            alt=""
                            src={"demo/photos/apart_example.jpg"}
                            className="h-8"
                          />
                          </Table.Col>
                      
                      </Table.Row>
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


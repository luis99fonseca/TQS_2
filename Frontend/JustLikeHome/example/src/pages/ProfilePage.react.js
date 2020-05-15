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
} from "tabler-react";

import SiteWrapper from "../SiteWrapper.react";
import CardTitle from "../components/Card/CardTitle.react";

export default class ProfilePage extends Component {
  constructor(props) {
      super(props);
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
                  <Comment
                    avatarURL="demo/faces/male/16.jpg"
                    name="Pedro Carvalho"
                    date="4 min"
                    text="Aenean lacinia bibendum nulla sed consectetur. Vestibulum id ligula porta felis euismod semper. Morbi leo risus, porta ac consectetur ac, vestibulum at eros. Cras justo odio, dapibus ac facilisis in, egestas eget quam. Vestibulum id ligula porta felis euismod semper. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus."
  
                  />
                   <Comment
                    avatarURL="demo/faces/male/12.jpg"
                    name="Ricardo Carvalho"
                    date="4 min"
                    text="Aenean lacinia bibendum nulla sed consectetur. Vestibulum id ligula porta felis euismod semper. Morbi leo risus, porta ac consectetur ac, vestibulum at eros. Cras justo odio, dapibus ac facilisis in, egestas eget quam. Vestibulum id ligula porta felis euismod semper. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus."
                  />

                </Comment.List>
              </Card>
              <Form className="card">
                <Card.Body>
                  <Card.Title>Edit Profile</Card.Title>
                  <Grid.Row>
                    <Grid.Col sm={6} md={3}>
                      <Form.Group>
                        <Form.Label>Username</Form.Label>
                        <Form.Input
                          type="text"
                          placeholder="Username"
                          value={username}
                        />
                      </Form.Group>
                    </Grid.Col>
                    <Grid.Col sm={6} md={4}>
                      <Form.Group>
                        <Form.Label>Email address</Form.Label>
                        <Form.Input type="email" placeholder="Email" />
                      </Form.Group>
                    </Grid.Col>
                    <Grid.Col sm={6} md={6}>
                      <Form.Group>
                        <Form.Label>First Name</Form.Label>
                        <Form.Input
                          type="text"
                          placeholder="First Name"
                          value={firstName}
                        />
                      </Form.Group>
                    </Grid.Col>
                    <Grid.Col sm={6} md={6}>
                      <Form.Group>
                        <Form.Label>Last Name</Form.Label>
                        <Form.Input
                          type="text"
                          placeholder="Last Name"
                          value={lastName}
                        />
                      </Form.Group>
                    </Grid.Col>
                    
                    <Grid.Col md={12}>
                      <Form.Group className="mb=0" label="About Me">
                        <Form.Textarea
                          rows={5}
                          placeholder="Here can be your description"
                        >
                          Oh so, your weak rhyme You doubt I'll bother, reading
                          into it I'll probably won't, left to my own devices
                          But that's the difference in our opinions.
                        </Form.Textarea>
                      </Form.Group>
                    </Grid.Col>
                  </Grid.Row>
                </Card.Body>
                <Card.Footer className="text-right">
                  <Button type="submit" color="primary">
                    Atualizar Profile
                  </Button>
                </Card.Footer>
              </Form>
            </Grid.Col>
          </Grid.Row>
        </Container>
      </div>
    </SiteWrapper>
  );
}}


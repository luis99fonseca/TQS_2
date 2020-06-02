// @flow
import React, { Component } from "react";

import { StandaloneFormPage, 
          FormCard,
          FormTextInput,
          Form
        } from "tabler-react";
import User from "../Rest/User"
import getDataForm from "../Rest/getDataForm";

import DatePicker from "react-datepicker";
 
import "react-datepicker/dist/react-datepicker.css";


export default class RegisterPage extends Component{

  constructor(props) {
    super(props);

    this.state = {
      startDate : new Date(),
      error : false,
      error_user: false,
      msg_error: "Tem que ter no mínimo 5 caracteres",
      msg_error_user: "Já existe!"
    }

    this.user_obj = new User()
    this.create_user = this.create_user.bind(this)
    this.initial_page = this.initial_page.bind(this)

}

initial_page() {
  window.location.href = '/'
  return true;
}

async create_user(event){
  event.preventDefault();
  let data = getDataForm(event.target);
  let response = await this.user_obj.create_user(data)
  let status = response[0]
  let user = response[1]

  console.log(status)
  console.log(user)
  if (status === 500){
    this.setState({
      error : true,
      error_user: false,
    })
  } else if (status === 404) {
    console.log("ola")
    this.setState({
      error_user : true,
      error: false,
    })
  } else {
    localStorage.setItem('user_id', user.id);
    localStorage.setItem("username", user.username);
    this.initial_page()
  }
} 
  


handleChange = date => {
  this.setState({
    startDate: date
  });
};

render(){
  return(
    <StandaloneFormPage imageURL={"demo/photos/icon_registo.jpg"}>

        <FormCard
        buttonText={"Criar"}
        title={"Registar"}
        onSubmit={this.create_user}
      >
        <FormTextInput
          name="username"
          label={"Username:"}
          placeholder={
            "Insira seu username"
          }
          required
        />
        <span style={{color: 'red', marginBottom:"35px"}}>{this.state.error_user && this.state.msg_error_user}</span>
        <FormTextInput
          name="firstName"
          label={"Primeiro Nome:"}
          placeholder={
            "Insira seu primeiro Nome:"
          }
          required
        />
        <FormTextInput
          name="lastName"
          label={"Último Nome:"}
          placeholder={
            "Insira seu último Nome:"
          }
          required
        />

        <Form.Label>Data de Nascimento:</Form.Label>
  
        <DatePicker
          name="birthDate"
          selected={this.state.startDate}
          dateFormat="dd-MM-yyyy"
          onChange={(date) => this.handleChange(date, "startDate")}
          />
          <div style={{marginTop:"20px"}}>

          </div>
        <FormTextInput
          name="password"
          type="password"
          label={"Password:"}
          placeholder={
            "Insira a sua password"
          }
        />
        <span style={{color: 'red'}}>{this.state.error && this.state.msg_error}</span>
      </FormCard>
    </StandaloneFormPage>
  )
}

}

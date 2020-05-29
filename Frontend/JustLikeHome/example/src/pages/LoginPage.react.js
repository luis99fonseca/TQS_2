// @flow

import React, { Component } from "react";

import { StandaloneFormPage, 
          FormCard,
          FormTextInput,
          Form,
          Button
        } from "tabler-react";
import User from "../Rest/User"
import getDataForm from "../Rest/getDataForm";

export default class LoginPage extends Component{

  constructor(props) {
    super(props);

    this.state = {
      error : false
    }
    this.user_obj = new User()
    this.login_user = this.login_user.bind(this)
    this.render_error = this.render_error.bind(this)
    this.redirect_registerPage = this.redirect_registerPage.bind(this)
  }

  async login_user(event){
    event.preventDefault();
    let data = getDataForm(event.target);
    console.log(data)
    let response = await this.user_obj.loginUser(data)
    let status = response[0]
    let user = response[1]
    
    if (status === 200){
     
      localStorage.setItem('user_id', user.userID);
      localStorage.setItem('username', data['username'])
      window.location.href = '/'    
    } else {
      this.setState({
        error: true
      })
    }
  }

  render_error(){
    return(
      <div>
          <span style={{color:"red"}}>Username ou password inválido!</span>
      </div>
    )
  }

  redirect_registerPage(){
    window.location.href = '/register' 
  }

  render(){
    return(

      <StandaloneFormPage imageURL={"demo/photos/icon_registo.jpg"}>

        <FormCard
        buttonText={"Entrar"}
        title={"Login"}
        onSubmit={this.login_user}
      >
        <FormTextInput
          name="username"
          label={"Username:"}
          placeholder={
            "Insira o seu username"
          }
          required
        />  
    
        <FormTextInput
          name="password"
          type="password"
          label={"Password:"}
          placeholder={
            "Insira a sua password"
          }
        />
        <a href="/register">Não tem conta?</a>
        {this.state.error === true && this.render_error()}
      </FormCard>
    </StandaloneFormPage>
    )
  }

}

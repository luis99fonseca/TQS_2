// https://www.npmjs.com/package/react-simple-image-slider
import SimpleImageSlider from "react-simple-image-slider";

import React, { Component, useCallback } from "react";
import { Page, Grid, GalleryCard, Form, Button, Container, Text, Card} from "tabler-react";
import SiteWrapper from "./SiteWrapper.react";

export default class Property extends Component {
    constructor(props) {
        super(props);
    }


    render() {
        const images = [
            { url: "demo/photos/example1-apart.jpg" },
            { url: "demo/photos/example2-apart.jpg" },
            { url: "demo/photos/example3-apart.jpg" },
        ];
        const avatarUrl = "demo/faces/female/7.jpg"
        const nameOwner = "João Artur"
        const numberOfPeople = "3";
        const beds = "2";
        const pricePerNight="300"
        const city = "Rua de Ovar Aveiro"
        const distanceCityCenter = "3km"
        const description = "Apartamento T2 novo, no centro, a 200 metros da praia, em prédio acabado de construir, de ótima qualidade, 2 frentes com muita luz natural, excelente área útil, móveis de cozinha com acabamento em alto brilho, pré-instalação de aquecimento central com caldeira incluída. \
        Localizado no centro da cidade, perto da praia e da estação, com tudo perto e bons acessos.\
        Garagem para um carro."
        const comedities = ["Garagem", "Piscina", "Internet 500mb/s de upload"]
        const reviews = [
            {reviewing:"João Baião", rating:"4", description:"Muito confortável, vista lindíssima!"},
            {reviewing:"Pedro Carvalho", rating:"5", description:"Senhorio muito simpático, melhor sítio onde já estive!"}
        ]
        const listComedities = comedities.map((c) => <li>{c}</li>)

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
            
            <h1 style={{fontSize:"70px", marginTop:"50px"}}>{city}</h1>
            
            

            <div style={{ borderBottom:"1px solid"}}>
                <h2>Propriedades</h2>
                 <p>Distância à cidade centro: {distanceCityCenter}</p> 
                <p>Número de pessoas disponíveis: {numberOfPeople} </p>
                <p>Número de quartos: {beds} </p>
            </div>
            <div style={{ borderBottom:"1px solid", marginTop:"50px"}}>
                <h2>Descrição</h2>
                <p>{description}</p>
            </div>
            <div style={{ borderBottom:"1px solid",marginTop:"50px"}}>
                <h2>Características</h2>
                <ul>{listComedities}</ul>
            </div>
            <div style={{ borderBottom:"1px solid",marginTop:"50px"}} class="row">
                <div class="col-lg-12">
                    <h2>Últimas Reviews:</h2>
                </div>
                {reviews.map((rev)=>{
                    return(
                    <div class="col-lg-6">
                        <Card key={rev.reviewing}>
                            <Card.Header>
                              <Card.Title>{rev.reviewing}</Card.Title>
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
            <form>
                <div class="row" style={{marginTop:"50px"}}>
                <div class="col-lg-2">
                    <GalleryCard.Details
                        avatarURL={avatarUrl}
                        fullName={nameOwner}
                    />
                </div>
               
                    <div class="col-lg-3">
                        <p>Data de Iníco:</p>
                        <Form.DatePicker
                    defaultDate={new Date("2020-05-13T16:06:07.669Z")}
                    format="mm/dd/yyyy"
                    required
                    maxYear={2030}
                    minYear={2020}
                    monthLabels={[
                      'Janeiro',
                      'Fevereiro',
                      'Março',
                      'Abril',
                      'Maio',
                      'Junho',
                      'Julho',
                      'Agosto',
                      'Setembro',
                      'Outubro',
                      'Novembro',
                      'Dezembro'
                    ]}
                  />
                    </div>
                    <div class="col-lg-3">
                        <p>Data de Fim:</p>
                        <Form.DatePicker
                    defaultDate={new Date("2020-05-13T16:06:07.669Z")}
                    format="mm/dd/yyyy"
                    required
                    maxYear={2030}
                    minYear={2020}
                    monthLabels={[
                      'Janeiro',
                      'Fevereiro',
                      'Março',
                      'Abril',
                      'Maio',
                      'Junho',
                      'Julho',
                      'Agosto',
                      'Setembro',
                      'Outubro',
                      'Novembro',
                      'Dezembro'
                    ]}
                  />
                    </div>
                    <div class="col-lg-4">
                        <h3 style={{fontSize:"20px"}}><b style={{fontSize:"40px"}}>{pricePerNight}€</b>/por noite</h3>
                        <button type="button" class="btn btn-primary" style={{size:"lg"}} >Alugar</button>
                    </div>
               
                </div>
            </form>

                    
                </Container>
            </SiteWrapper>
            

        );
    }

}
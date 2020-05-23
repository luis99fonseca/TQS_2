// @flow

import * as React from "react";
import Card from "./components/Card/Card.react"
import {
  Grid,
  BlogCard
} from "tabler-react";
import Icon from "./components/Icon";

import SiteWrapper from "./SiteWrapper.react";

function Home() {
  return (
    <SiteWrapper>
      <div class="container">
        <div style={{ "text-align":"center", "backgroundImage":'url(demo/photos/img-back.jpg)', "backgroundRepeat":"no-repeat", "backgroundPosition":"center"}}>
        <h1 style={{"fontSize":"150px", "paddingTop":"80px", "color":"white", textShadow:"2px 2px #000000"}}>Just Like Home</h1>
        </div>

        <Grid.Row cards deck>
            <div class={"col-lg-5"}>
                  <h3>
                    {"O que é o Just Like Home?"}
                  </h3>
                  <div className="text-muted" style={{ "fontSize":"20px"}}>
                    {"O Just Like Home conecta pessoas com lugares para ficar. A comunidade é alimentada por anfitriões, que arrendam os seus bens imóveis, para que outras pessoas possam encontrá-los mais facilmente, simplificando o processo de alugar."}
                  </div> 
            </div>
            <div class={"col-lg-5 offset-lg-2"}>
                <h3>
                  {"O que é hospedagem?"}
                </h3>
                <div className="text-muted" style={{ "fontSize":"20px"}}>
                  {"Se você tiver um quarto extra, uma casa inteira, poderá ganhar dinheiro compartilhando-o com qualquer pessoa no mundo. Você pode hospedar sua casa. Quando você hospeda, depende de você."}
                </div>
            </div>
            <div class={"col-lg-5"} style={{"paddingTop":"100px"}}>
                <h3>
                  {"Porquê criar um anúncio no Just Like Home?"}
                </h3>
                <div className="text-muted" style={{ "fontSize":"20px"}}>
                  {"Não importa que tipo de casa ou quarto você tenha para compartilhar, o Just Like Home torna simples e seguro o acolhimento dos hóspedes. Você controla totalmente sua disponibilidade, preços, regras da casa."}
                </div> 
            </div>
            <div class={"col-lg-5 offset-lg-2"} style={{"paddingTop":"100px"}}>
                <h3>
                  {"Encontre a sua residência ideal"}
                </h3>
                <div className="text-muted" style={{ "fontSize":"20px"}}>
                  {"O Just Like Home oferece-te uma pesquisa avançada onde poderá encontrar a casa que procura em poucos instantes."}
                </div>
            </div>
            <div class="col-lg-5" style={{"paddingTop":"150px"}}>
              <img src="demo/photos/senhoria6.jpg"  class=""/>
            </div>
            <div class="col-lg-5 offset-lg-1"  style={{"paddingTop":"300px"}}>
              <Icon prefix="fa" name="quote-left"></Icon>
              <h3>
                {"Just Like Home fez com que os meus apartamentos ganhassem visibilidade, fazendo com que aumentasse a aderência! "}
              </h3>
              {"Joana Ferreira membro hà 2 anos."}
            </div>


        </Grid.Row>

      </div>
    </SiteWrapper>
  );
}

export default Home;

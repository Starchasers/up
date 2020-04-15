import React from 'react'

import Layout from '../components/layout'
import Container from '../components/elements/Container'
import MainBox from '../components/blocks/MainBox'
import Grid from 'styled-components-grid'

const starchasers = '© ' + new Date().getFullYear() + ' Starchasers'

const IndexPage = () => (
  <Layout>
    <Container>
      <MainBox>
        <MainBox.Box></MainBox.Box>
        <Grid>
          <MainBox.Box square center>
            <MainBox.StyledLink href="https://github.com/HubertBalcerzak/up">
              <MainBox.Text>UP</MainBox.Text>
            </MainBox.StyledLink>
          </MainBox.Box>
          <MainBox.Text right light>
            <MainBox.List>
              <MainBox.ItemList>
                <MainBox.StyledLink href="https://github.com/HubertBalcerzak/up">
                  <MainBox.Text light>Github</MainBox.Text>
                </MainBox.StyledLink>
              </MainBox.ItemList>
              <MainBox.ItemList>
                <MainBox.StyledLink href="https://kuboczoch.pl">
                  <MainBox.Text light>My Website</MainBox.Text>
                </MainBox.StyledLink>
              </MainBox.ItemList>
              <MainBox.ItemList>
                <MainBox.StyledLink href="https://starchasers.pl">
                  <MainBox.Text light>{starchasers}</MainBox.Text>
                </MainBox.StyledLink>
              </MainBox.ItemList>
            </MainBox.List>
          </MainBox.Text>
        </Grid>
        <MainBox.Shadow />
      </MainBox>
    </Container>
  </Layout>
)

export default IndexPage

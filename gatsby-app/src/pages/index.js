import React from 'react'

import Layout from '../components/layout'
import Container from '../components/elements/Container'
import MainBox from '../components/blocks/MainBox'
import Grid from 'styled-components-grid'
import styled, { css } from 'styled-components'
import { breakpoint } from 'styled-components-breakpoint'
import { py } from 'styled-components-spacing/dist/cjs'

const starchasers = '© ' + new Date().getFullYear() + ' Starchasers'

const Mobile = css`
  display: flex;
  flex-direction: column;
  align-items: center;
  background-color: ${props => props.theme.colors.secondary.two};
  border-radius: ${props => props.theme.border.radius};
`

const Row = styled(Grid)`
  ${py({ xs: 4, sm: 0 })};
  ${breakpoint('xs', 'sm')`
    ${() => Mobile};
  `}
`

const IndexPage = () => (
  <Layout>
    <Container>
      <MainBox>
        <MainBox.Box/>
        <Row>
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
        </Row>
        <MainBox.Shadow />
      </MainBox>
    </Container>
  </Layout>
)

export default IndexPage

import React from 'react'
import Grid from 'styled-components-grid'
import styled, { css } from 'styled-components'
import { py } from 'styled-components-spacing/dist/cjs'
import { breakpoint } from 'styled-components-breakpoint'
import Link from 'gatsby-link'

import Layout from '../components/layout'
import Container from '../components/elements/Container'
import MainBox from '../components/blocks/MainBox'
import MainImage from '../components/elements/MainImage'
import InfoButton from '../components/elements/InfoButton'
import ErrorBox from '../components/blocks/ErrorBox'
import { faAngleLeft } from '@fortawesome/free-solid-svg-icons'

const Mobile = css`
  display: flex;
  flex-direction: column;
  align-items: center;
  background-color: ${props => props.theme.colors.secondary.two};
  border-radius: ${props => props.theme.border.radius};
`

const Row = styled(Grid)`
  ${py({ xs: 4, sm: 0 })};
  width: calc(100% - 50px);
  margin: 0 auto;
  position: relative;
  align-items: center;
  
  ${breakpoint('xs', 'sm')`
    ${() => Mobile};
  `}
`

const NotFoundPage = () => (
  <Layout>
    <Container>
      <MainBox>
        <MainBox.Box>
          <Grid>
            <Grid.Unit size={{ xs: 0, md: 1 / 3 }}>
              <MainImage/>
            </Grid.Unit>
            <Grid.Unit size={{ xs: 1, md: 2 / 3 }}>
              <ErrorBox>
                <ErrorBox.Header>Server kaput</ErrorBox.Header>
                <ErrorBox.Text>Oopsie woopsie,</ErrorBox.Text>
                <ErrorBox.Text>something went wrong</ErrorBox.Text>
                <ErrorBox.Back
                  as={Link}
                  to='/'
                >
                  <ErrorBox.Icon icon={faAngleLeft}/>
                  Go home
                </ErrorBox.Back>
              </ErrorBox>
            </Grid.Unit>
          </Grid>
        </MainBox.Box>
        <Row>
          <InfoButton/>
          <MainBox.Text right light>
            <MainBox.List>
              <MainBox.ItemList>
                <MainBox.StyledLink href="https://github.com/Starchasers/up">
                  <MainBox.Text light>GitHub</MainBox.Text>
                </MainBox.StyledLink>
              </MainBox.ItemList>
              <MainBox.ItemList>
                <MainBox.StyledLink href="https://github.com/Starchasers">
                  <MainBox.Text light>Starchasers</MainBox.Text>
                </MainBox.StyledLink>
              </MainBox.ItemList>
            </MainBox.List>
          </MainBox.Text>
        </Row>
        <MainBox.Shadow/>
      </MainBox>
    </Container>
  </Layout>
)

export default NotFoundPage

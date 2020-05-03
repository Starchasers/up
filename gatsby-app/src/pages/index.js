import React from 'react'
import Layout from '../components/layout'
import Container from '../components/elements/Container'
import MainBox from '../components/blocks/MainBox'
import Grid from 'styled-components-grid'
import styled, { css } from 'styled-components'
import { breakpoint } from 'styled-components-breakpoint'
import { py } from 'styled-components-spacing/dist/cjs'
import FileUpload from '../components/FileUpload'
import foreground from '../assets/images/foreground.jpg'
import AfterUploadBox from '../components/blocks/AfterUploadBox'
import Loader from '../components/elements/Loader'
import { faAngleLeft } from '@fortawesome/free-solid-svg-icons'
import ErrorBox from '../components/ErrorBox'
import { getResponseState } from '../redux/selectors'
import { connect } from 'react-redux'
import { setResponse } from '../redux/actions'

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

const Decoration = styled('div')`
  width: 100%;
  height: 100%;
  min-height: 300px;
  background-color: ${props => props.theme.colors.secondary.two};
  border-radius: ${props => props.theme.border.radius};
  background-image: url(${foreground});
  background-position: center;
  background-size: cover;
`

const IndexPage = ({ response, setResponse }) => {
  return (
    <Layout>
      <Container>
        <MainBox>
          <MainBox.Box>
            <Grid>
              <Grid.Unit size={{ xs: 0, md: 1 / 3 }}>
                <Decoration alt='Freepik.com'/>
              </Grid.Unit>
              <Grid.Unit size={{ xs: 1, md: 2 / 3 }}>
                <Loader>
                  <ErrorBox>
                    {
                      response.received
                        ? <AfterUploadBox>
                          <AfterUploadBox.TextBox>
                            <AfterUploadBox.Text
                              target='_blank'
                              href={`${process.env.GATSBY_API_URL
                                ? process.env.GATSBY_API_URL
                                : ''}/u/` + response.data.key}
                            >
                              {`${process.env.GATSBY_API_URL
                                ? process.env.GATSBY_API_URL
                                : window.location.origin}/u/` + response.data.key}
                            </AfterUploadBox.Text>
                          </AfterUploadBox.TextBox>
                          <AfterUploadBox.Back onClick={() => setResponse({ received: false, data: {} })}>
                            <AfterUploadBox.Icon icon={faAngleLeft}/>
                            Go back
                          </AfterUploadBox.Back>
                        </AfterUploadBox>
                        : <FileUpload/>
                    }
                  </ErrorBox>
                </Loader>
              </Grid.Unit>
            </Grid>
          </MainBox.Box>
          <Row>
            <MainBox.Box square center>
              <MainBox.StyledLink href="https://github.com/Starchasers/up">
                <MainBox.Text>UP</MainBox.Text>
              </MainBox.StyledLink>
            </MainBox.Box>
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
}

const mapStateToProps = state => {
  const response = getResponseState(state)
  return { response }
}

export default connect(mapStateToProps, { setResponse })(IndexPage)

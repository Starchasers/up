import React, { useState } from 'react'
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

const Decoration = styled('div')`
  width: 100%;
  height: 100%;
  min-height: 300px;
  background-color: ${props => props.theme.colors.secondary.two};
  border-radius: ${props => props.theme.border.radius};
  background-image: url(${foreground});
  background-position: left;
  background-size: cover;
`

const IndexPage = () => {
  const [loading, setLoading] = useState(false)
  const [upload, setUpload] = useState({ uploaded: false, data: {} })
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
                <Loader loading={loading}>
                  {
                    upload.uploaded
                      ? <AfterUploadBox>
                        <AfterUploadBox.TextBox>
                          <AfterUploadBox.Text
                            target='_blank'
                            href={`${process.env.GATSBY_API_URL
                              ? process.env.GATSBY_API_URL
                              : ''}/u/` + upload.data.key}
                          >
                            {`${process.env.GATSBY_API_URL
                              ? process.env.GATSBY_API_URL
                              : window.location.href}/u/` + upload.data.key}
                          </AfterUploadBox.Text>
                        </AfterUploadBox.TextBox>
                        <AfterUploadBox.Back onClick={() => setUpload({ uploaded: false, data: {} })}>
                          <AfterUploadBox.Icon icon={faAngleLeft}/>
                          Go back
                        </AfterUploadBox.Back>
                      </AfterUploadBox>
                      : <FileUpload
                        setLoading={setLoading}
                        setUpload={setUpload}
                      />
                  }
                </Loader>
              </Grid.Unit>
            </Grid>
          </MainBox.Box>
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
          <MainBox.Shadow/>
        </MainBox>
      </Container>
    </Layout>
  )
}

export default IndexPage

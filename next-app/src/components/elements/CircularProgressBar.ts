import styled from '@emotion/styled'
import { CircularProgressbar as Loader } from 'react-circular-progressbar'
import 'react-circular-progressbar/dist/styles.css'

const CircularProgressBar = styled(Loader)`
  max-width: 50%;
  margin: 0 auto;

  > .CircularProgressbar-text {
    dominant-baseline: central;
    fill: ${(props) => props.theme.colors.timberwolf};
    font-size: 15px;
  }

  > .CircularProgressbar-background {
    fill: ${(props) => props.theme.colors.shark};
  }

  > .CircularProgressbar-trail {
    stroke: ${(props) => props.theme.colors.shark};
  }

  > .CircularProgressbar-path {
    stroke: ${(props) => props.theme.colors.timberwolf};
  }

  @media (max-width: ${(props) => props.theme.breakpoints.xs}) {
    max-width: 75%;
  }
`

export default CircularProgressBar

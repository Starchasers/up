import styled from 'styled-components'

import StyledContainer from '../../elements/Container'

const Container = styled(StyledContainer)`
  display: grid;
  grid-template-columns: 1fr;
  margin-top: 150px;

  @media (max-width: ${props => props.theme.breakpoints.md}) {
    margin-top: 100px;
  }

  @media (max-width: ${props => props.theme.breakpoints.sm}) {
    margin-top: 0;
  }
`

export default Container

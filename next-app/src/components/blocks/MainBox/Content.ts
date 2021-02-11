import styled from 'styled-components'

const Content = styled('div')`
  padding: 25px;
  min-height: 25px;
  background: ${props => props.theme.palette.primary};
  border-radius: ${props => props.theme.constants.borderRadius};
`

export default Content

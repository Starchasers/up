import styled from 'styled-components'

const Text = styled('span')`
  text-align: center;
  justify-content: center;
  display: flex;
  color: ${props => props.theme.colors.secondary.one};
  font-weight: ${props => props.bold ? 'bold' : 'normal'};
`

export default Text

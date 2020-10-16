import styled from 'styled-components'

const Area = styled('div')`
  display: flex;
  flex-direction: column;
  justify-content: center;
  position: relative;
  background-color: ${props => props.background && props.theme.colors.secondary.two};
  border-radius: ${props => props.theme.border.radius};
`

export default Area

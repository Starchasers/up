import styled from 'styled-components'

const Container = styled('div')`
  position: relative;
  display: flex;
  cursor: pointer;
  border-radius: ${props => props.theme.border.radius};
  width: 80%;
  margin: 0 auto;
  align-items: center;
  flex-direction: column;
`

export default Container

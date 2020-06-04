import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import styled from 'styled-components'

const Icon = styled(FontAwesomeIcon)`
  color: ${props => props.theme.colors.primary.two};
  font-size: 30px;
  z-index: 10;
`

export default Icon

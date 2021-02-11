import styled from 'styled-components'

import theme from '../../../assets/theme'
import { includeVariant } from './variants'
import { changeOpacityOnLoading } from './LoaderContainer'

const ButtonText = styled('span')`
  font-weight: bold;
  font-size: 16px;
  line-height: 25px;
  text-align: center;
  overflow-wrap: break-word;
  transition: all ${theme.constants.transition.normal};

  ${props => props.isLoading && changeOpacityOnLoading};

  ${props => includeVariant(props, 'text')};
`

export default ButtonText

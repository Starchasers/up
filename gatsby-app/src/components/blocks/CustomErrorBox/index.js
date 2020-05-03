import styled from 'styled-components'
import Icon from './Icon'
import Back from './Back'
import { pl } from 'styled-components-spacing/dist/cjs'
import Pre from './Pre'
import Code from './Code'
import Header from './Header'
import Status from './Status'

const CustomErrorBox = styled('div')`
  width: 100%;
  display: flex;
  justify-content: center;
  flex-direction: column;
  min-height: 300px;
  position: relative;
  ${pl({md: 4})};
`

CustomErrorBox.Icon = Icon
CustomErrorBox.Back = Back
CustomErrorBox.Pre = Pre
CustomErrorBox.Code = Code
CustomErrorBox.Header = Header
CustomErrorBox.Status = Status

export default CustomErrorBox

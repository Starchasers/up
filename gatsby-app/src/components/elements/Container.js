import styled from 'styled-components'
import breakpoint from 'styled-components-breakpoint'

const Container = styled('div')`
  width: 100%;
  margin: 0 auto;
  min-height: 1px;

  ${breakpoint('sm')`width: 540px;`};
  ${breakpoint('md')`width: 720px;`};
  ${breakpoint('lg')`width: 960px;`};
  ${breakpoint('xl')`width: 1140px;`};
`

export default Container

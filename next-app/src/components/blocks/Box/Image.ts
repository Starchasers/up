import styled from '@emotion/styled'

const Image = styled('img')`
  width: 100%;
  // For now this is best fast solution
  height: 300px;
  background-color: rgb(31, 34, 41);
  border-radius: 8px;
  object-fit: cover;
  transition: opacity 500ms ease 0s;
  user-select: none;
  -webkit-user-drag: none;

  @media (max-width: ${(props) => props.theme.breakpoints.md}) {
    display: none;
  }
`

export default Image

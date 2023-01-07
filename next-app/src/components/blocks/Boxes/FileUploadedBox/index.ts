import styled from '@emotion/styled'

const FileUploadedBox = styled('div')`
  display: grid;
  grid-template-columns: 1fr;
  grid-template-rows: 128px auto 40px;
  grid-row-gap: 20px;

  padding: 0 45px;

  @media (max-width: ${(props) => props.theme.breakpoints.lg}) {
    padding: 0 25px;
  }

  @media (max-width: ${(props) => props.theme.breakpoints.md}) {
    padding: 0;
  }

  @media (max-width: ${(props) => props.theme.breakpoints.sm}) {
    grid-template-rows: auto 1fr auto;
    grid-row-gap: 10px;
  }
`

export default FileUploadedBox

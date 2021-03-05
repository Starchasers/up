import styled from 'styled-components'

const Icon = styled.span`
    background: #434755;
    color: ${props => props.theme.colors.secondary.one};
    padding: 3px 10px;
    text-align: center;
    border-radius: 50%;
    font-size: 20px;
    font-family: 'Bree Serif', serif;
    font-weight: 600;
    z-index: 1;
    user-select: none;
`
export default Icon

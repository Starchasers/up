import styled from 'styled-components'
import ProgressiveImage from 'react-progressive-image'
import foreground from '../../assets/images/foreground.jpg'
import tinyForeground from '../../assets/images/foreground-small.jpg'
import React from 'react'

const Decoration = styled('img')`
  width: 100%;
  height: 100%;
  min-height: 300px;
  background-color: ${props => props.theme.colors.secondary.two};
  border-radius: ${props => props.theme.border.radius};
  object-fit: cover;
  transition: opacity 500ms ease 0s;
  user-select: none;
  user-focus: none;
  user-drag: none;
`

const MainImage = () => (
  <ProgressiveImage src={foreground} placeholder={tinyForeground}>
    {(src, loading) => <Decoration src={src} alt='Main image' style={{ opacity: loading ? 0.75 : 1 }} />}
  </ProgressiveImage>
)

export default MainImage

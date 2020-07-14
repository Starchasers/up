import React from 'react'
import {
  List,
  Datagrid,
  TextField,
  EditButton
} from 'react-admin'

export default (props) => (
  <List
    {...props}
  >
    <Datagrid>
      <TextField
        source='id'
      />
      <TextField
        source='username'
      />
      <TextField
        source='email'
      />
      <TextField
        source='role'
      />
      <EditButton />
    </Datagrid>
  </List>
)

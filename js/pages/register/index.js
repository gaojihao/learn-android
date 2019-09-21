import React from 'react';
import {
  TouchableOpacity,
  StyleSheet,
  StatusBar,
  TextInput,
  Text,
  Image,
  View,
} from 'react-native';

import config from './../../tools/config';

let g_code_btn_state = true;

export default class RegisterScreen extends React.Component {
    static navigationOptions = {
      title: '注册',
    };

    constructor(props){
      super(props)

      this.state = {
        name:"",
        code:"",
        passwprd:"",
        count: 60,          //剩余时间
        setInter:null,      //存储计时器
      }
    }
  
    render() {
      return (
        <View style={styles.container}>
          <StatusBar backgroundColor="white" barStyle="dark-content" />
          <View style={styles.viewContainer}>
            <View style={styles.cellStyle}>
              <Image source={{uri: 'shoujihao'}} style={{width: 16, height: 16}}/>
              <TextInput
                style={styles.inputStyle}
                defaultValue={this.state.name}
                placeholder='请输入手机号'
                returnKeyType='next'
                selectionColor='white'
                underlineColorAndroid='transparent'
                placeholderTextColor='#999999'
                onChangeText={(text) => this.setState({ name: text })}
              />
            </View>
            <View style={styles.cellStyle}>
              <Image source={{uri: 'yanzhengma'}} style={{width: 16, height: 16}}/>
              <TextInput
                style={styles.inputStyle}
                defaultValue={this.state.code}
                placeholder='请输入验证码'
                returnKeyType='next'
                selectionColor='white'
                underlineColorAndroid='transparent'
                placeholderTextColor='#999999'
                onChangeText={(text) => this.setState({ code: text })}
              />
              <TouchableOpacity 
              disabled = {this.state.count===60? false:true} 
              style={this.state.count===60?styles.codeStyle:styles.codeStyleDisable} 
              activeOpacity={0.8} 
              onPress={this._onGetCode} >
                <Text style = {{color:'#FFF',fontSize:10}}>{this.state.count===60?'获取验证码':`剩余${this.state.count}s`}</Text>
              </TouchableOpacity>
            </View>
            <View style={styles.cellStyle}>
            <Image source={{uri: 'password'}} style={{width: 16, height: 16}}/>
              <TextInput
                style={styles.inputStyle}
                defaultValue={this.state.passwprd}
                placeholder='请输入登录密码'
                returnKeyType='next'
                selectionColor='white'
                secureTextEntry={true}
                underlineColorAndroid='transparent'
                placeholderTextColor='#999999'
                onChangeText={(text) => this.setState({ passwprd: text })}
              />
            </View>

            <TouchableOpacity style={styles.signStyle} activeOpacity={0.8} onPress={this._onCommit} >
              <Text style = {{color:'white',fontSize:15}}>提交</Text>
            </TouchableOpacity>
          </View>
        </View>
      );
    }

    _onCommit = () => {
      this.props.navigation.navigate('SignIn');
    };

    _onGetCode = () => {
      if(!g_code_btn_state){
        return
      }

      g_code_btn_state = false;
      const { name } = this.state
      let that = this;

      that.state.setInter = setInterval(() => {
        let numVal = that.state.count -1;
        if (numVal === 0){
          g_code_btn_state = true;
          clearInterval(this.state.setInter)
          that.setState(() => ({ count: 60 }));
        }else{
          that.setState(() => ({ count: numVal }));
        }
  
      },1000)


    }

  }

  const styles = StyleSheet.create({
    container: {
      width:config.screenWidth,
      height:config.screenHeight,
      backgroundColor:'white',
      alignItems: 'center',
      justifyContent: 'space-between',
    },

    viewContainer: {
      flex: 1,
      backgroundColor:'white',
      width:config.screenWidth - 40,
      alignItems: 'center',
      marginTop:40,
    },

    cellStyle: {
      backgroundColor:'white',
      flexDirection:'row',
      width:config.screenWidth - 40,
      height:44,
      marginBottom:20,
      alignItems: 'center',
      borderBottomColor:'#efefef',
      borderBottomWidth:config.onePix,
    },

    inputStyle:{
      height:40,
      color:'#000000',
      flex:1,
      alignItems:'center',
      justifyContent:'center',
      marginLeft:4,
      textAlign:'left',
  },

    signStyle: {
      backgroundColor:'#FC427B',
      height:44,
      width:config.screenWidth - 40,
      marginTop:30,
      alignItems: 'center',
      justifyContent: 'center',
    },

    codeStyle: {
      backgroundColor:'#FC427B',
      height:30,
      width:80,
      alignItems: 'center',
      justifyContent: 'center',
    },

    codeStyleDisable: {
      backgroundColor:'#a1a1a1',
      height:30,
      width:80,
      alignItems: 'center',
      justifyContent: 'center',
    },

  });
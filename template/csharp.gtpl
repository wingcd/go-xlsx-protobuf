// DO NOT EDIT!
// This code is auto generated by go-xlsx-exporter
// VERSION {{.Version}}
// go-protobuf {{.GoProtoVersion}}

{{- $NS := .Namespace}}

using System;
using System.Collections.Generic;
using ProtoBuf;
using System.IO;
{{- range .Info.Imports}}
{{.}}
{{- end}}

namespace {{.Namespace}}
{
    {{- /*生成枚举类型*/}}
    {{- range .Enums}}
    // Defined in table: {{.DefinedTable}}
    [Serializable]
    [ProtoContract]
    public enum {{.TypeName}}
    { {{range .Items}}
        {{if ne .Desc ""}} /* {{.Desc}} */ {{end}}
        [ProtoEnum]
        {{.FieldName}} = {{.Value}}, 
    {{end -}}
    }
    {{end}}

    {{- /*生成结构体类型*/}}
    {{- range .Structs}}
    // Defined in table: {{.DefinedTable}}
    [Serializable]
    [ProtoContract]
    public struct {{.TypeName}}
    { {{range .Items}}
        {{if ne .Desc ""}} /* {{.Desc}} */ {{end}}
        [ProtoMember({{.Index}})]
        {{- if .IsArray}}
        public List<{{.ValueType}}> {{.FieldName}} { get; set; }
        {{- else}}
        public {{.ValueType}} {{.FieldName}} { get; set; }
        {{end -}}
    {{end}}
    }
    {{end}}

    {{- /*生成配置类类型*/}}
    {{- range .Consts}}
    // Defined in table: {{.DefinedTable}}
    [Serializable]
    [ProtoContract]
    public class {{.TypeName}} : PBDataModel
    { {{range .Items}}
        {{- if not .IsVoid }}
            {{- if ne .Desc ""}} /* {{.Desc}} */ {{end}}
        [ProtoMember({{.Index}})]
            {{- if .IsArray}}
        public List<{{.ValueType}}> {{.FieldName}} { get; set; }
            {{- else}}
        public {{.ValueType}} {{.FieldName}} { get; set; }
            {{end -}}
        {{- end}}
        {{- if .Convertable}}
        public {{get_alias .Alias}} Get{{camel_case .FieldName}}()
        {
            return ({{get_alias .Alias}})GetConvertData("{{.FieldName}}", {{.FieldName}}, "{{get_alias .Alias}}", {{.Cachable}});
        }
        {{- end}}
    {{end}}
    }
    {{end}}

    {{- /*生成类类型*/}}
    {{- range .Tables}}
    
    // Defined in table: {{.DefinedTable}}
    [Serializable]
    [ProtoContract]
    public class {{.TypeName}} : {{if .IsArray}}PBDataModels{{else}}PBDataModel{{end}}
    { 
    {{range .Headers}}
        {{- $fieldName := camel_case .FieldName -}}
    {{- if not .IsVoid }}
        {{if ne .Desc ""}} /* {{.Desc}} */ {{end}}
        [ProtoMember({{.Index}})]
        {{- if .IsArray}}
        public List<{{.ValueType}}> {{$fieldName}} { get; set; }
        {{- else }}
        public {{.ValueType}} {{$fieldName}} { get; set; }
        {{- end}}
        {{- if .Convertable}}
        public {{get_alias .Alias}} Get{{$fieldName}}()
        {
            return ({{get_alias .Alias}})GetConvertData("{{$fieldName}}", {{$fieldName}}, "{{get_alias .Alias}}", {{.Cachable}});
        }
        {{- end}}
    {{- else}}    
        {{- if .Convertable}}
        public {{get_alias .Alias}} Get{{$fieldName}}()
        {
            return ({{get_alias .Alias}})GetConvertData("{{$fieldName}}", null, "{{get_alias .Alias}}", {{.Cachable}});
        }
        {{- end}}
    {{- end}}
    {{- end}}
    }
    {{- end}}

    {{- if .HasMessage}}    
    // regist all messages

    public enum EMessageNames {
        {{- range .Tables}}
            {{- if is_message_table .TableType}}
                {{- if gt .Id 0}}
        E_MSG_{{.TypeName}} = {{.Id}},
                {{- end}}
            {{- end}}
        {{- end}} 
    }

    public static class MessageFactory
    {    
        public static Dictionary<int, Type> Types = new Dictionary<int, Type>()
        {
        {{- range .Tables}}
            {{- if is_message_table .TableType}}
                {{- if gt .Id 0}}
            { {{.Id}}, typeof({{.TypeName}}) },
                {{- end}}
            {{- end}}
        {{- end}}  
        }; 

        public static PBDataModel CreateMessage(int id)
        {
            if(!Types.ContainsKey(id)) 
            {                
                return null;
            }

            return Activator.CreateInstance(Types[id]) as PBDataModel;
        }

        public static PBDataModel LoadMessage(int id, byte[] bytes)
        {
            if(!Types.ContainsKey(id)) 
            {                
                return null;
            }

            using (var stream = new MemoryStream(bytes))
            {
                object data = Serializer.NonGeneric.Deserialize(Types[id], stream);
                return data as PBDataModel;
            }
        }
    }
    {{- end}}
}

{{- /*生成类类型*/}}
public partial class ConfigManager
{
    {{- range .Tables}}
    {{- if not .IsArray}}
    public static DataContainer<uint, {{$NS}}.{{.TypeName}}> {{.TypeName}} = new ();
    {{- end}}
    {{- end}}
}
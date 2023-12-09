extends Node

var _thinkingdata = null

func _ready():
    if Engine.has_singleton("GodotThinkingData"):
        _thinkingdata = Engine.get_singleton("GodotThinkingData")
    else:
        push_warning('GodotThinkingData plugin not found!')


func init(appId: String, serverUrl:String) -> void:
    if _thinkingdata != null:
        _thinkingdata.init(appId, serverUrl)
        print('GodotThinkingData plugin inited!')
        
func login(userId:String)->void:
    if _thinkingdata != null:
    	_thinkingdata.login(userId)

func sendEvent(key:String, value:String, eventName:String)
	if _thinkingdata != null:
		_thinkingdata.sendEvent(key,value,eventName)

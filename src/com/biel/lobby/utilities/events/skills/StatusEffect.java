package com.biel.lobby.utilities.events.skills;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.biel.BielAPI.events.PlayerWorldEventBus;
import com.biel.lobby.Com;
import com.biel.lobby.Mapa;
import com.biel.lobby.mapes.Joc;
import com.biel.lobby.mapes.Joc.PlayerInfo;
import com.biel.lobby.utilities.Utils;

public abstract class StatusEffect extends PlayerWorldEventBus {
	private int remainingTicks = -1; // -1 -> Forever
	private double value = -1; // -1 -> Forever
	private double maxValue = -1; // -1 -> Forever
	private String ownerName;
	private boolean modal = false;
	private int modalRemainingTicks = -1; // -1 -> Never
	public int id = Utils.NombreEntre(0, 100);
	private StatusEffectType type = StatusEffectType.UNDEFINED;
	public enum StatusEffectType{UNDEFINED, SKILL_TRAY, BUFF, DEBUFF}
	public StatusEffect(Player ply) {
		super(ply);
		// TODO Auto-generated constructor stub
	}
	public abstract String getName();
	public abstract String getDescription();
	public double getValue() {
		return value;
	}
	public void setValue(double value) {
		this.value = value;
	}
	public double getMaxValue() {
		return maxValue;
	}
	public void setMaxValue(double maxValue) {
		this.maxValue = maxValue;
	}
	public Player getOwnerPlayer() {
		if(ownerName == null)return null;
		return Bukkit.getPlayer(ownerName);
	}

	public void setOwnerPlayer(Player player) {
		this.ownerName = player.getName();
	}
	public StatusEffectType getType() {
		return type;
	}
	public void setType(StatusEffectType type) {
		this.type = type;
	}
	public boolean isModal() {
		return modal;
	}
	public void setModal(boolean modal) {
		this.modal = modal;
		if(!modal)setModalRemainingTicks(-1); //-1 -> Never!
	}
	public int getModalRemainingTicks() {
		return modalRemainingTicks;
	}
	public void setModalRemainingTicks(int modalRemainingTicks) {
		this.modalRemainingTicks = modalRemainingTicks;
		if(modalRemainingTicks > 0)setModal(true);
	}
	public ChatColor getChatColor(){
		ChatColor c = ChatColor.GRAY;
		switch(getType()){
		case BUFF:
			c = ChatColor.GREEN;
			if(modal)c = ChatColor.DARK_GREEN;
			break;
		case DEBUFF:
			c = ChatColor.RED;
			if(modal)c = ChatColor.DARK_RED;
			break;
		case SKILL_TRAY:
			c = ChatColor.DARK_AQUA;
			if(modal)c = ChatColor.AQUA;
			break;
		case UNDEFINED:
			if(modal)c = ChatColor.BLACK;
			break;
		default:
			break;		
		}
		return c;
	}
	public String getDisplayText() { //Per deshabilitar sobreescriure a null
		String cdString = getRemainingSecondsString();
		String valueString = getValueString();
		String modalHeading = "";
		if(modal)modalHeading += ChatColor.BOLD;
		String r = ChatColor.RESET + "" + getChatColor() + modalHeading + getName() + " " + valueString + " " + cdString;
		return r.trim() + " id=" + id;
	}
	public String getRemainingSecondsString() {
		if(remainingTicks == -1)return "";
		return "(" + (Math.round(getRemainingSeconds() * 10) / 10.0) + "s)";
	}
	public String getValueString() {
		if(value != -1 && maxValue != -1){
			return "(" + Math.round(getValue()) + "/" + Math.round(getMaxValue()) + ")";			
		}
		if(value != -1 && maxValue == -1){
			return "(" + Math.round(getValue()) + ")";			
		}
		return "";
	}
	public int getRemainingTicks() {
		return remainingTicks;
	}
	public void setRemainingTicks(int remainingTicks) {
		this.remainingTicks = remainingTicks;
	}
	public double getRemainingSeconds(){
		return getRemainingTicks() / getTickSpacing();
	}
	@Override
	public boolean isValid() {
		// Quan arribi a 0 fora!
		return remainingTicks > 0;
	}
	public void tick() {
		if(remainingTicks == 0)return;
		if(remainingTicks > 0)remainingTicks -= getTickSpacing();
		if(remainingTicks < 0)remainingTicks = 0;
		if(modalRemainingTicks == 0)setModal(false);
		if(modalRemainingTicks > 0)modalRemainingTicks -= getTickSpacing();
		if(modalRemainingTicks < 0)modalRemainingTicks = 0;
	}
	private static double getTickSpacing(){
		return 20;
	}
	public boolean hasExpired(){
		if(remainingTicks == -1)return false;
		return remainingTicks <= 0;
	}
	//GAME-WRAPPING
		protected Joc getGame(){
			Mapa mapWherePlayerIs = Com.getPlugin().gest.getMapWherePlayerIs(getPlayer());
			if(mapWherePlayerIs == null)return null;
			if(mapWherePlayerIs instanceof Joc){
				return (Joc) mapWherePlayerIs;			
			}
			return null;
		}
		protected void sendGlobalMessage(String message){
			getGame().sendGlobalMessage(message);
		}
		protected void sendPlayerMessage(Player p, String message){
			getGame().sendPlayerMessage(p, message);
		}
		protected void sendPlayerMessage(String message){
			sendPlayerMessage(getPlayer(), message);
		}
		protected void sendEffectMessage(String message){
			sendEffectMessage(getPlayer(), message);
		}
		protected void sendEffectMessage(Player p, String message){
			sendPlayerMessage(p, ChatColor.DARK_AQUA + "{" + getName() + "} > " + ChatColor.GRAY + message);
		}
		public PlayerInfo getPlayerInfo(Player p) {
			return getGame().getPlayerInfo(p);
		}
}

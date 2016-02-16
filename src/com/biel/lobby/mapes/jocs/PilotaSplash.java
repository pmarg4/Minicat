package com.biel.lobby.mapes.jocs;

import java.util.ArrayList;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.biel.BielAPI.Utils.GUtils;
import com.biel.lobby.mapes.JocScoreRace;

import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.MobDisguise;
import me.libraryaddict.disguise.disguisetypes.watchers.SlimeWatcher;

public class PilotaSplash extends JocScoreRace {

	@Override
	protected int getFinishScore() {
		// TODO Auto-generated method stub
		return 20;
	}

	@Override
	public String getGameName() {
		// TODO Auto-generated method stub
		return "Pilota Splash";
	}
	@Override
	public void JocIniciat() {
		// TODO Auto-generated method stub
		super.JocIniciat();
		applyDisguises();
	}
	@Override
	protected void donarEfectesInicials(Player ply) {
		// TODO Auto-generated method stub
		super.donarEfectesInicials(ply);
		//ply.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, true), true);
		ply.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 4, true), true);

	}
	@Override
	protected ArrayList<ItemStack> getStartingItems(Player ply) {
		ArrayList<ItemStack> items = new ArrayList<ItemStack>();
		ItemStack item = new ItemStack(Material.SLIME_BALL, 1);
		item.addUnsafeEnchantment(Enchantment.KNOCKBACK, 2);
		items.add(GUtils.setItemNameAndLore(item, "Puny de pilota", "Pilotassa"));
		return items;
	}
	@Override
	protected int getBaseSkillUnlockerAmount() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	protected void teletransportarTothom() {
		for (Player d : getPlayers()) {  // d gets successively each value in ar.
			teleportToRandomSpawn(d);					
		} 
	}
	@Override
	protected void onPlayerDamage(EntityDamageEvent evt, Player p) {
		// TODO Auto-generated method stub
		super.onPlayerDamage(evt, p);
		if(evt.getCause() == DamageCause.FALL){
			evt.setCancelled(true);
			GUtils.healDamageable(p, 1.0);
			getWorld().playSound(p.getLocation(), (evt.getDamage() > 4 ? Sound.SLIME_WALK2 : Sound.SLIME_WALK), 1F, 1F);			
		}
	}
	@Override
	protected void onPlayerMove(PlayerMoveEvent evt, Player p) {
		// TODO Auto-generated method stub
		super.onPlayerMove(evt, p);
		if(evt.getTo().getY() < getMinimumHeight()){
			teleportToRandomSpawn(p);
			Player k = getPlayerInfo(p).getLastDamager();
			incrementScore(k);
			resetSpree(p);
			k.playSound(p.getLocation(), Sound.SLIME_ATTACK, 1F, 1.5F);			
			k.playEffect(p.getEyeLocation(), Effect.POTION_SWIRL, 3);
		}
	}
	@Override
	protected void onPlayerDeathByPlayer(PlayerDeathEvent evt, Player killed, Player killer) {
		// TODO Auto-generated method stub
		super.onPlayerDeathByPlayer(evt, killed, killer);
		incrementScore(killer);
	}
	@Override
	protected void setSpree(Player ply, int value) {
		// TODO Auto-generated method stub
		super.setSpree(ply, value);
//		SlimeWatcher w = (SlimeWatcher) DisguiseAPI.getDisguise(ply).getWatcher();
//		w.setSize(value);		
	}
	//Disguises
	void applyDisguises(){
		for(Player p : getPlayers()){
//			MobDisguise d = new MobDisguise(DisguiseType.SLIME);
//			d.setKeepDisguiseOnPlayerDeath(true);
//			d.setKeepDisguiseOnPlayerLogout(false);
//			SlimeWatcher w = (SlimeWatcher) d.getWatcher();
//			w.setSize(1);
//			DisguiseAPI.disguiseToAll(p, d);//DisguiseAPI.
		}
	}
}

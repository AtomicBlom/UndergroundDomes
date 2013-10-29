package net.binaryvibrance.undergrounddomes.tileentitity;

import java.util.HashMap;

import net.binaryvibrance.net.undergrounddomes.api.IPhotonEmitter;
import net.binaryvibrance.net.undergrounddomes.api.IPhotonPowered;
import net.binaryvibrance.undergrounddomes.block.BlockLightReceptor;
import net.binaryvibrance.undergrounddomes.generation.maths.Point3D;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.EnumFacing;

public class TileLightReceptor extends TileEntity implements IPhotonPowered, IPhotonEmitter {
	HashMap<EnumFacing, Float> LightLevels = new HashMap<EnumFacing, Float>();

	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound nbtTag = new NBTTagCompound();
		this.writeToNBT(nbtTag);
		return new Packet132TileEntityData(xCoord, yCoord, zCoord, 1, nbtTag);
	}

	@Override
	public void onDataPacket(INetworkManager net, Packet132TileEntityData packet) {
		readFromNBT(packet.data);
	}

	@Override
	public void writeToNBT(NBTTagCompound par1) {
		super.writeToNBT(par1);
	}

	@Override
	public void readFromNBT(NBTTagCompound par1) {
		super.readFromNBT(par1);
	}

	@Override
	public void updateEntity() {
		if (worldObj != null && !worldObj.isRemote && worldObj.getTotalWorldTime() % 20L == 0L) {
			blockType = this.getBlockType();

			if (blockType != null && blockType instanceof BlockLightReceptor) {
				((BlockLightReceptor) blockType).updateLightLevel(new Point3D(xCoord, yCoord, zCoord, worldObj));
			}
		}
	}

	@Override
	public void GetPhotonLevel(Direction inverseDirection, float powerLevel) {

	}

	@Override
	public void OnAdjacentPhotonLevelChanged(Direction direction, float powerLevel) {

	}
}

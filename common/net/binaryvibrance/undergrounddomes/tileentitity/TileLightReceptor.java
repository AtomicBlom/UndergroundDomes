package net.binaryvibrance.undergrounddomes.tileentitity;

import net.binaryvibrance.undergrounddomes.block.BlockLightReceptor;
import net.binaryvibrance.undergrounddomes.generation.maths.Point3D;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;

public class TileLightReceptor extends TileEntity {
	public Packet getDescriptionPacket() {
		NBTTagCompound nbtTag = new NBTTagCompound();
		this.writeToNBT(nbtTag);
		return new Packet132TileEntityData(this.xCoord, this.yCoord,
				this.zCoord, 1, nbtTag);
	}

	public void onDataPacket(INetworkManager net, Packet132TileEntityData packet) {
		readFromNBT(packet.customParam1);
	}

	public void writeToNBT(NBTTagCompound par1) {
		super.writeToNBT(par1);
	}

	public void readFromNBT(NBTTagCompound par1) {
		super.readFromNBT(par1);
	}
	
	@Override
	public void updateEntity() {
		if (this.worldObj != null && !this.worldObj.isRemote && this.worldObj.getTotalWorldTime() % 20L == 0L)
        {
            this.blockType = this.getBlockType();

            if (this.blockType != null && this.blockType instanceof BlockLightReceptor)
            {
                ((BlockLightReceptor)this.blockType).updateLightLevel(new Point3D(this.xCoord, this.yCoord, this.zCoord, this.worldObj));
            }
        }
	}	
}

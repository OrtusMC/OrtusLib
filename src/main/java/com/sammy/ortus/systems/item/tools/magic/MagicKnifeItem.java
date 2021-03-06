package com.sammy.ortus.systems.item.tools.magic;

import com.google.common.collect.ImmutableMultimap;
import com.sammy.ortus.systems.item.tools.OrtusKnifeItem;
import com.sammy.ortus.setup.OrtusAttributeRegistry;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Tier;

public class MagicKnifeItem extends OrtusKnifeItem {
    public final float magicDamage;

    public MagicKnifeItem(Tier tier, float attackDamageIn, float attackSpeedIn, float magicDamage, Properties properties) {
        super(tier, attackDamageIn, attackSpeedIn, properties);
        this.magicDamage = magicDamage;
    }

    @Override
    public ImmutableMultimap.Builder<Attribute, AttributeModifier> createExtraAttributes() {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = new ImmutableMultimap.Builder<>();
        builder.put(OrtusAttributeRegistry.MAGIC_DAMAGE.get(), new AttributeModifier(OrtusAttributeRegistry.UUIDS.get(OrtusAttributeRegistry.MAGIC_DAMAGE), "Weapon magic damage", magicDamage, AttributeModifier.Operation.ADDITION));
        return builder;
    }
}

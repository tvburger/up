package net.tvburger.up.client;

import net.tvburger.up.behaviors.ManagedEntity;

import java.io.Closeable;

public interface UpClientManager extends Closeable, ManagedEntity.Manager<UpClientInfo> {
}

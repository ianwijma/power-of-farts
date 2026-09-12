# Power Of Farts — Early Game Concept Plan

Eat food → your body produces gas → collect it, store it, pipe it, burn it for power.

## Locked design decisions

| Decision | Choice |
|---|---|
| Gas Bag collection | Active use — right-click to de-gass into bag |
| Fart cloud | Affects everyone nearby, including you |
| Other-mod gases | Fluids-as-gas via `#poweroffarts:gas_fluids` tag; Mekanism bridge later |
| Economy | Balanced, config-driven |

## Architecture

One internal gas + power system in `common/`, thin adapters at the loader boundary (the AE2 pattern):

```
common/                        gas/ player/ item/ machine/ registry/ integration/
fabric/                        TR EnergyStorage adapter, Storage API lookups
neoforge/                      EnergyHandler (neoforge.transfer) adapters, capabilities
```

- Gas is a first-class type (`poweroffarts:farts`), measured in **FL** (Fart Liters). Not a fluid.
- Power: common internal buffer; NeoForge `EnergyHandler` / Fabric TR `EnergyStorage` adapters.
- Registry abstraction via ServiceLoader (`IPlatformRegistry`), vanilla `MappedRegistry` for Gas.
- Config: common JSON at `config/poweroffarts.json` (Gson), loader-agnostic.

## The systems

1. **Food → potential gas**: common mixin on `finishUsingItem`; `nutrition + saturation × gasPerHunger → pendingGas`.
2. **Potential → stored**: per-player tick, pending digests into stored at `digestionRatePerSecond`. Data attachment per loader.
3. **Overflow fart**: `stored ≥ capacity` → vanilla `AreaEffectCloud` (green, nausea 5s, radius 3, everyone incl. you), dump excess, cooldown.
4. **Gas Bag**: right-click sucks `storedGas` into the bag; contents in item data component; fill bar + FL tooltip; stack size 1.

## The early-game blocks (tier-ready)

| Block | Behavior | Slow tuning |
|---|---|---|
| Gas Depositor | Bag in (right-click) → internal buffer → pushes to adjacent pipes/tanks | 2 FL/t |
| Gas Pipe | Push-based point-to-point network, accepts any gas (incl. gas-fluids) | 5 FL/t per connection |
| Gas Tank | Insert top, drain bottom, sides = pipe IO, fill rendering | 8,000 FL |
| Fart Generator | Burns gas → power, pushes to neighbors | 4 FL/t → 20 FE/t |

## Config defaults (`config/poweroffarts.json`)

```json
{
  "gasPerHunger": 10.0,
  "playerGasCapacity": 100.0,
  "digestionRatePerSecond": 0.5,
  "fartCloudRadius": 3.0,
  "fartCloudDurationSeconds": 10,
  "nauseaSeconds": 5,
  "gasBagCapacity": 500.0,
  "gasTankCapacity": 8000.0,
  "depositorTransferRate": 2.0,
  "pipeTransferRate": 5.0,
  "generatorBurnRate": 4.0,
  "generatorPowerOutput": 20.0
}
```

## Phases

1. **Infra** — registration service, Gas/GasStack/registry, config, energy buffer. ✅
2. **Core loop** — digestion mixin, player gas data, fart cloud, Gas Bag. Playtestable. ✅
3. **Machines** — Depositor → Pipe → Tank → Generator + Jade tooltips. ✅ Validated via `/pof flowtest` on both loaders (500 FL transport conserved; 100 FL → 500 FE burn).
4. **Polish** — ✅ Recipes (bag + all 4 machines), proper item/block textures, fluid-as-gas interop:
   - `#poweroffarts:gas_fluids` tag (`minecraft:lava` as placeholder; modpacks add real gas fluids)
   - Foreign pumps push tagged fluids into our pipes/tanks (1 mB = 1 FL → `FluidGas`), foreign tanks receive FluidGas back
   - Exposed via NeoForge `ResourceHandler<FluidResource>` / Fabric `Storage<FluidVariant>`
   - Validated via `/pof fluidtest` on both loaders (tag rejection, inbound conversion, outbound bridge)
   - Known limitation: fluid adapters mutate directly (no transaction rollback for foreign mods' aborted transactions)
5. **QA & HUD fixes** — NeoForge registration fix, creative tab, HUD + sync, `/pof` debug suite. ✅

# NBT Recipes

Adds the ability to add NBT data to the result of JSON recipes, but keeps the ability to read recipes with NBT data. Also adds the ability to set a result count for smelting, stonecutting, and smithing.

## Example Crafting JSON
```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "#"
  ],
  "key": {
    "#": {
      "item": "minecraft:diamond"
    }
  },
  "result": {
    "item": "minecraft:amethyst_shard",
    "count": 3,
    "data": {
      "display": {
        "Name": "[{\"text\":\"Example Item\",\"italic\":false}]",
        "Lore": [
          "[{\"text\":\"I am an example\",\"italic\":false}]"
        ]
      },
      "Enchantments": [{}]
    }
  }
}
```

## Example Smelting JSON
```json
{
  "type": "minecraft:smelting",
  "ingredient": {
    "item": "minecraft:diamond"
  },
  "result": {
    "item": "minecraft:amethyst_shard",
    "count": 3,
    "data": {
      "display": {
        "Name": "[{\"text\":\"Example Item\",\"italic\":false}]",
        "Lore": [
          "[{\"text\":\"I am an example\",\"italic\":false}]"
        ]
      },
      "Enchantments": [{}]
    }
  },
  "experience": 1,
  "cookingtime": 20
}
```

## Exmaple Stonecutting JSON
```json
{
  "type": "minecraft:stonecutting",
  "ingredient": {
    "item": "minecraft:diamond"
  },
  "result": {
    "item": "minecraft:amethyst_shard",
    "count": 3,
    "data": {
      "display": {
        "Name": "[{\"text\":\"Example Item\",\"italic\":false}]",
        "Lore": [
          "[{\"text\":\"I am an example\",\"italic\":false}]"
        ]
      },
      "Enchantments": [{}]
    }
  }
}
```

## Example Smithing JSON
```json
{
  "type": "minecraft:smithing_transform",
  "template": {
    "item": "minecraft:stick"
  },
  "base": {
    "item": "minecraft:gold_ingot"
  },
  "addition": {
    "item": "minecraft:diamond"
  },
  "result": {
    "item": "minecraft:amethyst_shard",
    "count": 3,
    "data": {
      "display": {
        "Name": "[{\"text\":\"Example Item\",\"italic\":false}]",
        "Lore": [
          "[{\"text\":\"I am an example\",\"italic\":false}]"
        ]
      },
      "Enchantments": [{}]
    }
  }
}
```
